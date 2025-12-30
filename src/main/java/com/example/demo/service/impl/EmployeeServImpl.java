package com.example.demo.service.impl;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.example.demo.service.ICategoryService;
import com.example.demo.service.ICompanyService;
import com.example.demo.service.IEmployeeHistoryService;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.IEmployeeTrainingService;

@Service("empserv")
public class EmployeeServImpl implements IEmployeeService {

	private final EmployeeRepository emprepo;
	private final DesignationRepository desigrepo;
	private final DepartmentRepository deptrepo;
	private final ICategoryService categoryserv;
	private final IEmployeeTrainingService emptrainserv;
	private final ActivityRepository activityrepo;
	private final IEmployeeHistoryService emphistserv;
	private final ICompanyService compserv;

	public EmployeeServImpl(EmployeeRepository emprepo, DesignationRepository desigrepo, DepartmentRepository deptrepo,
			ICategoryService categoryserv, IEmployeeTrainingService emptrainserv, ActivityRepository activityrepo,
			IEmployeeHistoryService emphistserv, ICompanyService compserv) {
		super();
		this.emprepo = emprepo;
		this.desigrepo = desigrepo;
		this.deptrepo = deptrepo;
		this.categoryserv = categoryserv;
		this.emptrainserv = emptrainserv;
		this.activityrepo = activityrepo;
		this.emphistserv = emphistserv;
		this.compserv = compserv;
	}

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
//	@Transactional
	public int updateEmployee(Employee emp) {

		Employee updatedEmployee = emprepo.save(emp);

		if (updatedEmployee != null) {
			EmployeeHistory emphist = new EmployeeHistory();
			emphist.setEmployee(updatedEmployee);

			if (updatedEmployee.getCategory() != null) {
				emphist.setCategory(updatedEmployee.getCategory().getCategory());
			} else {
				emphist.setCategory("");
			}
			if (updatedEmployee.getDesignation() != null) {
				emphist.setDesigName(updatedEmployee.getDesignation().getDesigName());
			} else {
				emphist.setDesigName("");
			}

			if (updatedEmployee.getDepartment() != null) {
				emphist.setDeptName(updatedEmployee.getDepartment().getDeptName());
				emphist.setCompName(updatedEmployee.getDepartment().getCompany().getCompName());
			} else {
				emphist.setDeptName("");
				emphist.setCompName("");
			}
			emphist.setContractorName(updatedEmployee.getContractorName());
			emphist.setJoiningDate(updatedEmployee.getJoiningDate());
			emphist.setEmpCode(updatedEmployee.getEmpCode());
			emphist.setStatus(updatedEmployee.getStatus());
			emphist.setEmpName(updatedEmployee.getEmpName());

			emphistserv.saveEmployeeHistory(emphist);

			Activity activity = Activity.builder().activity("Company " + emp.getEmpName() + " is saved successfully")
					.activityDate(dday.format(LocalDateTime.now())).activityTime(ttime.format(LocalDateTime.now()))
					.build();
			activityrepo.save(activity);
			return 1;
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

//		List<Training> trainList = emprepo.getAllTrainingsByEmployeeId(empid);
//		return trainList;
		return null;
	}

	@Override
	public void uploadEmployeeList(InputStream is) {

		try (Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);

				Employee emp = new Employee();

				 
				Optional<Employee> byEmp_code = emprepo.findByEmpCode(getCellValue(row.getCell(0)));
				if (!byEmp_code.isPresent()) {

					emp.setEmpName(getCellValue(row.getCell(0)));
					emp.setEmpCode(getCellValue(row.getCell(1)));

					Designation desig = null;
					desig = desigrepo.findByDesigName(getCellValue(row.getCell(2)));

					emp.setDesignation(desig);

					String dept_name = getCellValue(row.getCell(3));
					String comp_name = getCellValue(row.getCell(4));
					Department dept = null;
					Company comp = null;

					if (!dept_name.equals("")) {

						dept = deptrepo.getDepartmentByDeptNameAndCompanyName(dept_name.trim(), comp_name.trim());
					}
					if (!comp_name.equals("")) {
						comp = compserv.getCompanyByName(comp_name);
					}

					emp.setDepartment(dept);

					emp.setJoiningDate(getCellValue(row.getCell(5)));
					emp.setContractorName(getCellValue(row.getCell(6)));

					String categoryValue = getCellValue(row.getCell(7));
					Category category = null;
					if (!categoryValue.equals("")) {
						category = categoryserv.getCategoryByCategoryName(categoryValue);
					}

					emp.setCategory(category);
					emp.setStatus(1);

					Employee uploadedEmployee = emprepo.save(emp);
					if (uploadedEmployee != null) {
						EmployeeHistory empHist = new EmployeeHistory();

						empHist.setEmpName(uploadedEmployee.getEmpName());
						empHist.setContractorName(uploadedEmployee.getContractorName());
						if (category != null) {
							empHist.setCategory(uploadedEmployee.getCategory().getCategory());
						} else {
							empHist.setCategory("");
						}

						if (desig != null) {
							empHist.setDesigName(uploadedEmployee.getDesignation().getDesigName());
						} else {
							empHist.setDesigName("");
						}
						empHist.setEmployee(uploadedEmployee);
						empHist.setJoiningDate(uploadedEmployee.getJoiningDate());
						empHist.setEmpCode(uploadedEmployee.getEmpCode());

						if (dept != null) {
							empHist.setDeptName(uploadedEmployee.getDepartment().getDeptName());
							empHist.setCompName(uploadedEmployee.getDepartment().getCompany().getCompName());
						} else {
							empHist.setDeptName("");
							if (comp != null) {
								empHist.setCompName(comp.getCompName());
							} else {
								empHist.setCompName("");
							}
						}
						empHist.setStatus(uploadedEmployee.getStatus());
						emphistserv.saveEmployeeHistory(empHist);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Fail to parse Excel file: " + e.getMessage(), e);
		}
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
				empdto.setContractorName(emp.getContractorName());
				if (emp.getStatus() == 1) {
					empdto.setStatus("Active");
				} else {
					empdto.setStatus("InActive");
				}
				empdto.setIsTrainingGiven(count > 0);
				return empdto;
//		        	return EmployeeMapper.EmployeeToEmployeeDTO(emp, new EmployeeDTO());
			});
		}

		Map<String, Object> result = new HashMap<>();
		result.put("recordsTotal", emprepo.count());
		result.put("recordsFiltered", empdtos.getTotalElements());
		result.put("data", empdtos.getContent());

		return result;
	}
 
	public Page<Employee> searchEmployees(String search, Pageable pageable) {
//        Department departmentByDeptName = deptrepo.getDepartmentByDeptName(search);
//        System.err.println();
//		return emprepo.findByEmpNameContainingIgnoreCaseOrEmpCodeOrJoiningDateOrContractorNameContainingIgnoreCaseOrDesignationOrDepartment(
//                search, search,search,search,desigrepo.findByDesigName(search),departmentByDeptName, pageable);

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
//	public List<EmployeeDTO> getAllEmployeesWithoudTrainingAndCompetency(Long training_id, Long competency_id) {
//		var elist = emprepo.getAllEmployeesNotHaveTrainingAndCompetency(training_id, competency_id);
	public List<EmployeeDTO> getAllEmployeesWithoudTrainingAndCompetency(Long training_id, Long competency_id,String tdate, Long timeslot) {
		var elist = emprepo.getAllEmployeesNotHaveTrainingAndCompetency(training_id, competency_id,tdate,timeslot);
		
		if (elist.size() > 0) {
			List<EmployeeDTO> collect = elist.stream().map(emp -> {

				EmployeeDTO empdto = new EmployeeDTO();

				empdto.setEmpId(emp.getEmpId());
				empdto.setEmpName(emp.getEmpName());
				
				System.err.println("Found Employee "+empdto.toString());
				
				return empdto;

			}).collect(Collectors.toList());
			return collect;
		} else {
			throw new ResourceNotFoundException("No Employees found !!!");
		}
	}
}
