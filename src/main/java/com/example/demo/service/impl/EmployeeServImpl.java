package com.example.demo.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Activity;
import com.example.demo.entity.Category;
import com.example.demo.entity.Company;
import com.example.demo.entity.Department;
import com.example.demo.entity.Designation;
import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeHistory;
import com.example.demo.entity.Training;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.ActivityRepository;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.DesignationRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.EmployeeTrainingHistoryRepository;
import com.example.demo.service.ICategoryService;
import com.example.demo.service.ICompanyService;
import com.example.demo.service.IEmployeeHistoryService;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.IEmployeeTrainingService;

import lombok.RequiredArgsConstructor;

@Service("empserv")
@RequiredArgsConstructor
public class EmployeeServImpl implements IEmployeeService {

	private final EmployeeRepository emprepo;
	private final DesignationRepository desigrepo;
	private final DepartmentRepository deptrepo;
	private final ICategoryService categoryserv;
	private final IEmployeeTrainingService emptrainserv;
	private final ActivityRepository activityrepo;
	private final IEmployeeHistoryService emphistserv;
	private final ICompanyService compserv;
	private final EmployeeTrainingHistoryRepository emptrainhistrepo;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private DateTimeFormatter dday = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private DateTimeFormatter ttime = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public Employee saveEmployee(Employee emp) {

		Optional<Employee> byEmpCode = emprepo.findByEmpCode(emp.getEmpCode().trim());

		if (!byEmpCode.isPresent()) {

			Employee savedEmployee = emprepo.save(emp);
			if (savedEmployee != null) {

				EmployeeHistory emphist = new EmployeeHistory();

				Department dept = deptrepo.findById(savedEmployee.getDepartment().getDeptId()).orElse(null);

				Designation desig = desigrepo.findById(savedEmployee.getDesignation().getDesigId()).orElse(null);

				Category category = categoryserv.getCategoryById(savedEmployee.getCategory().getCategory_id());

				emphist.setEmployee(savedEmployee);
				emphist.setCategory(category.getCategory());
				emphist.setCompName(dept.getCompany().getCompName());
				emphist.setContractorName(savedEmployee.getContractorName());
				emphist.setDeptName(dept.getDeptName());
				emphist.setDesigName(desig.getDesigName());
				emphist.setEmpCode(savedEmployee.getEmpCode());
				emphist.setJoiningDate(savedEmployee.getJoiningDate());
				emphist.setStatus(savedEmployee.getStatus());
				emphist.setLeaveDate("");

				emphistserv.saveEmployeeHistory(emphist);

				Activity activity = Activity.builder()
						.activity("Company " + savedEmployee.getEmpName() + " is saved successfully")
						.activityDate(dday.format(LocalDateTime.now())).activityTime(ttime.format(LocalDateTime.now()))
						.build();
				activityrepo.save(activity);
				return savedEmployee;
			} else {
				throw new GlobalException(
						"Employee " + emp.getEmpName() + " is not saved and No trainings are provided");
			}
		} else {
			throw new GlobalException(
					"Employee " + emp.getEmpName() + " is already present with the Employee Code " + emp.getEmpCode());
		}
	}

	@Override
	public Employee getEmployeeByEmployeeId(Long empid) {

		return emprepo.findById(empid)
				.orElseThrow(() -> new ResourceNotFoundException("No Employee found for given ID " + empid));
	}

	@Override
	@Transactional
	public int updateEmployee(Employee emp) {

		String leaveDate = "";

		if (emp.getStatus() == 0) {
			leaveDate = dday.format(LocalDateTime.now());
		}

		int result = emprepo.updateEmployee(emp.getEmpId(), emp.getEmpName(), emp.getEmpCode(), emp.getContractorName(),
				emp.getCategory().getCategory_id(), emp.getJoiningDate(), emp.getDepartment().getDeptId(),
				emp.getDesignation().getDesigId(), emp.getStatus(), leaveDate);
		if (result > 0) {
			EmployeeHistory emphist = new EmployeeHistory();
			emphist.setEmployee(emp);

			if (emp.getCategory() != null) {
				Category category = categoryserv.getCategoryById(emp.getCategory().getCategory_id());
				emphist.setCategory(category.getCategory());
			} else {
				emphist.setCategory("");
			}
			if (emp.getDesignation() != null) {
				var desigObj = desigrepo.findById(emp.getDesignation().getDesigId());
				emphist.setDesigName(desigObj.get().getDesigName());
			} else {
				emphist.setDesigName("");
			}

			if (emp.getDepartment() != null) {
				Optional<Department> deptObj = deptrepo.findById(emp.getDepartment().getDeptId());
				if (deptObj.isPresent()) {
					emphist.setDeptName(deptObj.get().getDeptName());
					emphist.setCompName(deptObj.get().getCompany().getCompName());
				}
			} else {
				emphist.setDeptName("");
				emphist.setCompName("");
			}
			emphist.setContractorName(emp.getContractorName());
			emphist.setJoiningDate(emp.getJoiningDate());
			emphist.setEmpCode(emp.getEmpCode());
			emphist.setStatus(emp.getStatus());
			emphist.setEmpName(emp.getEmpName());

			emphist.setLeaveDate(leaveDate);

			emphistserv.saveEmployeeHistory(emphist);

			Activity activity = Activity.builder().activity("Company " + emp.getEmpName() + " is saved successfully")
					.activityDate(dday.format(LocalDateTime.now())).activityTime(ttime.format(LocalDateTime.now()))
					.build();
			activityrepo.save(activity);
			return result;
		}

		else {
			Activity activity = Activity.builder()
					.activity("Company " + emp.getEmpName() + " is not updated successfully")
					.activityDate(dday.format(LocalDateTime.now())).activityTime(ttime.format(LocalDateTime.now()))
					.build();
			activityrepo.save(activity);
			throw new ResourceNotModifiedException("Employee " + emp.getEmpName() + " is not Updated");
		}
	}

	@Override
	public List<Employee> getAllEmployees() {

		var elist = emprepo.findAll();

		if (elist.size() > 0) {
			return elist;
		} else {
			throw new ResourceNotFoundException("No Employees found !!!");
		}
	}

	@Override
	public Employee getEmployeeByEmployeeCode(String empcode) {
		var emp = emprepo.getEmployeeByCode(empcode);
		if (emp != null) {
			return emp;
		} else {
			throw new ResourceNotFoundException("No Employee found for given Employee Code " + empcode);
		}
	}

	@Override
	public List<Training> getAllTrainingsByEmployeeId(Long empid) {

		// List<Training> trainList = emprepo.getAllTrainingsByEmployeeId(empid);
		// return trainList;
		return null;
	}

	/** Number of Excel rows processed in a single DB transaction. */
	private static final int BATCH_SIZE = 500;

	/**
	 * Uploads employees from an Excel file in batches of {@value #BATCH_SIZE} rows.
	 * <p>
	 * Annotated with {@code @Async} so it runs in a dedicated background thread
	 * ("uploadTaskExecutor" pool). The controller returns HTTP 202 Accepted
	 * immediately while this method processes in the background.
	 * </p>
	 * <p>
	 * Accepts {@code byte[]} (not InputStream) because the MultipartFile stream
	 * is tied to the HTTP request and is closed once the request ends. Passing
	 * raw bytes makes the data available to this background thread regardless of
	 * request lifecycle.
	 * </p>
	 *
	 * @param fileBytes raw bytes of the uploaded .xlsx file
	 */
	@Async("uploadTaskExecutor")
	@Override
	public void uploadEmployeeList(byte[] fileBytes) {

		logger.info("[Thread: {}] uploadEmployeeList started – file size: {} bytes",
				Thread.currentThread().getName(), fileBytes.length);

		// Wrap bytes in a fresh InputStream – safe to use in this background thread.
		try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(fileBytes))) {

			Sheet sheet = workbook.getSheetAt(0);
			int lastRow = sheet.getLastRowNum();

			// Collect raw row data first (strings only – no DB calls yet).
			List<String[]> rowData = new ArrayList<>(lastRow);
			for (int i = 1; i <= lastRow; i++) {
				Row row = sheet.getRow(i);
				if (row == null) continue;
				rowData.add(new String[] {
						getCellValue(row.getCell(0)), // empName
						getCellValue(row.getCell(1)), // empCode
						getCellValue(row.getCell(2)), // designation
						getCellValue(row.getCell(3)), // dept
						getCellValue(row.getCell(4)), // company
						getCellValue(row.getCell(5)), // joiningDate
						getCellValue(row.getCell(6)), // contractorName
						getCellValue(row.getCell(7))  // category
				});
			}

			logger.info("uploadEmployeeList: {} data rows read from Excel", rowData.size());

			// Process in fixed-size batches – each batch has its own transaction.
			int totalSaved = 0;
			int totalSkipped = 0;
			for (int start = 0; start < rowData.size(); start += BATCH_SIZE) {
				int end = Math.min(start + BATCH_SIZE, rowData.size());
				List<String[]> batch = rowData.subList(start, end);
				int[] counts = processBatch(batch);
				totalSaved   += counts[0];
				totalSkipped += counts[1];
				logger.info("uploadEmployeeList: batch {}-{} done – saved={}, skipped={}",
						start + 1, end, counts[0], counts[1]);
			}

			logger.info("[Thread: {}] uploadEmployeeList complete – totalSaved={}, totalSkipped={}",
					Thread.currentThread().getName(), totalSaved, totalSkipped);

		} catch (Exception e) {
			logger.error("[Thread: {}] uploadEmployeeList FAILED: {}",
					Thread.currentThread().getName(), e.getMessage(), e);
			throw new RuntimeException("Fail to parse Excel file: " + e.getMessage(), e);
		}
	}

	/**
	 * Saves one batch of employees inside a single transaction.
	 * Lookup results (designation, department, company, category) are cached
	 * within the batch to avoid repeated round-trips for common values.
	 *
	 * @param batch list of String arrays, one per Excel row
	 * @return int[]{saved, skipped}
	 */
	@Transactional
	public int[] processBatch(List<String[]> batch) {

		// Per-batch caches to avoid repeated DB lookups for the same values.
		Map<String, Designation> desigCache    = new HashMap<>();
		Map<String, Department>  deptCache     = new HashMap<>();
		Map<String, Company>     compCache     = new HashMap<>();
		Map<String, Category>    categoryCache = new HashMap<>();

		int saved   = 0;
		int skipped = 0;

		for (String[] cols : batch) {
			String empCode = cols[1].trim();
			if (empCode.isEmpty()) {
				skipped++;
				continue;
			}

			// Skip duplicates without hitting the DB unnecessarily.
			if (emprepo.findByEmpCode(empCode).isPresent()) {
				skipped++;
				continue;
			}

			Employee emp = new Employee();
			emp.setEmpName(cols[0]);
			emp.setEmpCode(empCode);
			emp.setJoiningDate(cols[5]);
			emp.setContractorName(cols[6]);
			emp.setStatus(1);

			// --- Designation (cached) ---
			String desigName = cols[2].trim();
			Designation desig = desigCache.computeIfAbsent(
					desigName,
					k -> k.isEmpty() ? null : desigrepo.findByDesigName(k));
			emp.setDesignation(desig);

			// --- Department + Company (cached) ---
			String deptName = cols[3].trim();
			String compName = cols[4].trim();

			String deptKey = deptName + "||" + compName;
			Department dept = deptCache.computeIfAbsent(
					deptKey,
					k -> (deptName.isEmpty()) ? null
							: deptrepo.getDepartmentByDeptNameAndCompanyName(deptName, compName));
			emp.setDepartment(dept);

			Company comp = compCache.computeIfAbsent(
					compName,
					k -> k.isEmpty() ? null : compserv.getCompanyByName(k));

			// --- Category (cached) ---
			String categoryValue = cols[7].trim();
			Category category = categoryCache.computeIfAbsent(
					categoryValue,
					k -> k.isEmpty() ? null : categoryserv.getCategoryByCategoryName(k));
			emp.setCategory(category);

			// Persist employee.
			Employee uploadedEmployee = emprepo.save(emp);

			// Build history record.
			EmployeeHistory empHist = new EmployeeHistory();
			empHist.setEmpName(uploadedEmployee.getEmpName());
			empHist.setContractorName(uploadedEmployee.getContractorName());
			empHist.setCategory(category != null ? uploadedEmployee.getCategory().getCategory() : "");
			empHist.setDesigName(desig   != null ? uploadedEmployee.getDesignation().getDesigName() : "");
			empHist.setEmployee(uploadedEmployee);
			empHist.setJoiningDate(uploadedEmployee.getJoiningDate());
			empHist.setEmpCode(uploadedEmployee.getEmpCode());
			empHist.setStatus(uploadedEmployee.getStatus());
			empHist.setLeaveDate("");

			if (dept != null) {
				empHist.setDeptName(uploadedEmployee.getDepartment().getDeptName());
				empHist.setCompName(uploadedEmployee.getDepartment().getCompany().getCompName());
			} else {
				empHist.setDeptName("");
				empHist.setCompName(comp != null ? comp.getCompName() : "");
			}

			emphistserv.saveEmployeeHistory(empHist);
			saved++;
		}

		return new int[]{saved, skipped};
	}

	// Helper method to handle null/empty cells safely
	private static String getCellValue(Cell cell) {
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue().trim();
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue().toString();
				} else {
					return String.valueOf((long) cell.getNumericCellValue());
				}
			case BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			case FORMULA:
				return cell.getCellFormula();
			case BLANK:
			default:
				return "";
		}
	}

	@Override
	public Map<String, Object> getAllEmployeesWithPagination(int start, int length, String search, String orderColumn,
			String orderDir) {

		int page = start / length; // convert DataTables start -> page index

		Page<EmployeeDTO> empdtos = null;
		// ✅ Default sort
		Sort sort = Sort.by(Sort.Direction.ASC, "empId");

		if (orderColumn.equals("company")) {
			orderColumn = "comp_name";
		}

		// ✅ Apply DataTables sorting
		if (orderColumn != null && !orderColumn.isEmpty()) {
			Sort.Direction direction = "desc".equalsIgnoreCase(orderDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
			sort = Sort.by(direction, orderColumn);
		}

		Pageable pageable = PageRequest.of(page, length, sort);

		Page<Employee> employees;

		if (search != null && !search.isEmpty()) {
			search.trim();
			employees = this.searchEmployees(search, pageable);

			empdtos = employees.map(emp -> {
				EmployeeDTO empdto = new EmployeeDTO();

				int count = emptrainserv.countTrainingByEmpId(emp.getEmpId());
				empdto.setEmpId(emp.getEmpId());
				empdto.setEmpName(emp.getEmpName());
				empdto.setJoiningDate(emp.getJoiningDate());
				empdto.setEmpCode(emp.getEmpCode());
				if (emp.getDesignation() != null) {
					empdto.setDesignation(emp.getDesignation().getDesigName());
				} else {
					empdto.setDesignation("");
				}
				if (emp.getDepartment() != null) {
					empdto.setDepartment(emp.getDepartment().getDeptName());
					empdto.setCompany(emp.getDepartment().getCompany().getCompName());
				} else {
					empdto.setDepartment("");
					empdto.setCompany("");
				}
				if (emp.getLeaveDate() == null || emp.getLeaveDate() == "") {
					empdto.setLeaveDate("");
				} else {
					empdto.setLeaveDate(emp.getLeaveDate());
				}

				empdto.setContractorName(emp.getContractorName());

				if (emp.getStatus() == 1) {
					empdto.setStatus("Active");
				} else {
					empdto.setStatus("InActive");
				}

				empdto.setIsTrainingGiven(count > 0);
				return empdto;

			});
		} else {

			employees = emprepo.findAll(pageable);

			empdtos = employees.map(emp -> {
				EmployeeDTO empdto = new EmployeeDTO();

				int count = emptrainserv.countTrainingByEmpId(emp.getEmpId());
				empdto.setEmpId(emp.getEmpId());
				empdto.setEmpName(emp.getEmpName());
				empdto.setJoiningDate(emp.getJoiningDate());
				empdto.setEmpCode(emp.getEmpCode());
				if (emp.getDesignation() != null) {
					empdto.setDesignation(emp.getDesignation().getDesigName());
				} else {
					empdto.setDesignation("");
				}
				if (emp.getDepartment() != null) {
					empdto.setDepartment(emp.getDepartment().getDeptName());
					empdto.setCompany(emp.getDepartment().getCompany().getCompName());
				} else {
					empdto.setDepartment("");
					empdto.setCompany("");
				}
				if (emp.getLeaveDate() == null || emp.getLeaveDate() == "") {
					empdto.setLeaveDate("");
				} else {
					empdto.setLeaveDate(emp.getLeaveDate());
				}
				empdto.setContractorName(emp.getContractorName());
				if (emp.getStatus() == 1) {
					empdto.setStatus("Active");
				} else {
					empdto.setStatus("InActive");
				}
				empdto.setIsTrainingGiven(count > 0);
				return empdto;
			});
		}

		Map<String, Object> result = new HashMap<>();
		result.put("recordsTotal", emprepo.count());
		result.put("recordsFiltered", empdtos.getTotalElements());
		result.put("data", empdtos.getContent());

		return result;
	}

	public Page<Employee> searchEmployees(String search, Pageable pageable) {
		// Department departmentByDeptName = deptrepo.getDepartmentByDeptName(search);
		// System.err.println();
		// return
		// emprepo.findByEmpNameContainingIgnoreCaseOrEmpCodeOrJoiningDateOrContractorNameContainingIgnoreCaseOrDesignationOrDepartment(
		// search,
		// search,search,search,desigrepo.findByDesigName(search),departmentByDeptName,
		// pageable);

		Page<Employee> searchEmployees = emprepo.searchEmployees(search, pageable);
		return searchEmployees;
	}

	@Override
	public List<Employee> getAllActiveEmployees() {

		List<Employee> activeEmpList = emprepo.findByStatus(1);
		if (activeEmpList.size() > 0) {
			return activeEmpList;
		}
		throw new ResourceNotFoundException("No Active Employees found");
	}

	@Override
	public List<EmployeeDTO> getAllEmployeesWithoudTrainingAndCompetency(Long training_id, Long competency_id,
			String tdate, Long timeslot) {
		var elist = emprepo.getAllEmployeesNotHaveTrainingAndCompetency(training_id, competency_id, tdate, timeslot);

		if (elist.size() > 0) {
			List<EmployeeDTO> collect = elist.stream().map(emp -> {

				EmployeeDTO empdto = new EmployeeDTO();

				empdto.setEmpId(emp.getEmpId());
				empdto.setEmpName(emp.getEmpName());
				empdto.setEmpCode(emp.getEmpCode());

				return empdto;

			}).collect(Collectors.toList());
			return collect;
		} else {
			throw new ResourceNotFoundException("No Employees found !!!");
		}
	}

	@Override
	public int checkEmployeeAttendedTrainingOnDateAndTimeSlot(Long id, Long timeslot, String training_date) {
		return emptrainhistrepo.checkEmployeeAttendedTrainingOnDateAndTimeSlot(id, timeslot, training_date);
	}
}
