package com.example.demo.service.impl;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.Department;
import com.example.demo.entity.Designation;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Training;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.DesignationRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.ICategoryService;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.IEmployeeTrainingService;

@Service("empserv")
public class EmployeeServImpl implements IEmployeeService {

	private final EmployeeRepository emprepo;
	private final DesignationRepository desigrepo;
	private final DepartmentRepository deptrepo;
	private final ICategoryService categoryserv;	 
	private final IEmployeeTrainingService emptrainserv;

	public EmployeeServImpl(EmployeeRepository emprepo, DesignationRepository desigrepo, DepartmentRepository deptrepo,
			ICategoryService categoryserv, IEmployeeTrainingService emptrainserv) {
		super();
		this.emprepo = emprepo;
		this.desigrepo = desigrepo;
		this.deptrepo = deptrepo;
		this.categoryserv = categoryserv;
		this.emptrainserv = emptrainserv;
	}

	private Logger logger = LoggerFactory.getLogger(getClass());

	private DateTimeFormatter dday = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private DateTimeFormatter ttime = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public Employee saveEmployee(Employee emp) {

		Employee employeeByCode = emprepo.getEmployeeByCode(emp.getEmpCode());

		Employee savedEmployee = emprepo.save(emp);
		if (savedEmployee != null) {
			return savedEmployee;
		} else {
			throw new GlobalException("Employee " + emp.getEmpName() + " is not saved and No trainings are provided");
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

//		int result = emprepo.updateEmployee(emp.getEmp_id(), emp.getEmp_name(), emp.getEmp_code(),
//				emp.getContractor_name(), emp.getCategory().getCategory_id(), emp.getJoining_date(),
//				emp.getDepartment().getDept_id(), emp.getDesignation().getDesig_id());
		
		Employee updatedEmployee = emprepo.save(emp);
		
		if (updatedEmployee!=null ) {
			System.err.println("employee Updated ");
			return 1;
		} else {
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

				//Optional<Employee> byEmp_name = emprepo.findByEmp_name(getCellValue(row.getCell(0)));
				Optional<Employee> byEmp_code = emprepo.findByEmpCode(getCellValue(row.getCell(0)));
				if(!byEmp_code.isPresent()) {					

				emp.setEmpName(getCellValue(row.getCell(0)));
				emp.setEmpCode(getCellValue(row.getCell(1)));

				Designation desig = desigrepo.findByDesigName(getCellValue(row.getCell(2)));
				emp.setDesignation(desig);
				String dept_name = getCellValue(row.getCell(3));
				String comp_name = getCellValue(row.getCell(4));
				Department dept = deptrepo.getDepartmentByDeptNameAndCompanyName(dept_name.trim(), comp_name.trim());

				emp.setDepartment(dept);

				emp.setJoiningDate(getCellValue(row.getCell(5)));
				emp.setContractorName(getCellValue(row.getCell(6)));

				Category category = categoryserv.getCategoryByCategoryName(getCellValue(row.getCell(7)));

				emp.setCategory(category);
				
				emprepo.save(emp);
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
	public Map<String, Object> getAllEmployeesWithPagination(int start, int length,String search,String orderColumn,String orderDir) {
		    
		 	int page = start / length; // convert DataTables start -> page index
		 	
		 	 Page<EmployeeDTO> empdtos = null;
		    // ✅ Default sort
		    Sort sort = Sort.by(Sort.Direction.ASC, "empId");
		    
		    if(orderColumn.equals("company")) {
		    	orderColumn= "comp_name";
		    }

		    // ✅ Apply DataTables sorting
		    if (orderColumn != null && !orderColumn.isEmpty()) {
		        Sort.Direction direction = "desc".equalsIgnoreCase(orderDir)
		                ? Sort.Direction.DESC
		                : Sort.Direction.ASC;
		        sort = Sort.by(direction, orderColumn);
		    }

		    Pageable pageable = PageRequest.of(page, length, sort);

		    Page<Employee> employees;
		    
		    
		    if (search != null && !search.isEmpty()) {
		        employees = this.searchEmployees(search, pageable);
		        
		        empdtos  = employees.map(emp-> {
		        	EmployeeDTO empdto =new EmployeeDTO();
		        	
		        	int count = emptrainserv.countTrainingByEmpId(emp.getEmpId());
		        	empdto.setEmpId(emp.getEmpId());
		        	empdto.setEmpName(emp.getEmpName());
		        	empdto.setJoiningDate(emp.getJoiningDate());
		        	empdto.setEmpCode(emp.getEmpCode());
		        	empdto.setDesignation(emp.getDesignation().getDesigName());
		        	empdto.setDepartment(emp.getDepartment().getDeptName());
		        	empdto.setCompany(emp.getDepartment().getCompany().getCompName());
		        	empdto.setContractorName(emp.getContractorName());

		        	empdto.setIsTrainingGiven(count > 0);
		        	return empdto;

		        });
		    }
		    else {

		        employees = emprepo.findAll(pageable);

		        empdtos  = employees.map(emp-> {
		        	EmployeeDTO empdto =new EmployeeDTO();

		        	int count = emptrainserv.countTrainingByEmpId(emp.getEmpId());
		        	empdto.setEmpId(emp.getEmpId());
		        	empdto.setEmpName(emp.getEmpName());
		        	empdto.setJoiningDate(emp.getJoiningDate());
		        	empdto.setEmpCode(emp.getEmpCode());
		        	empdto.setDesignation(emp.getDesignation().getDesigName());
		        	empdto.setDepartment(emp.getDepartment().getDeptName());
		        	empdto.setCompany(emp.getDepartment().getCompany().getCompName());
		        	empdto.setContractorName(emp.getContractorName());

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
}
