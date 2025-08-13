
package com.example.demo.service.impl;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Category;
import com.example.demo.entity.Company;
import com.example.demo.entity.Department;
import com.example.demo.entity.Designation;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Training;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.DesignationRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.EmployeeTrainingHistoryRepository;
import com.example.demo.repository.TrainingRepository;
import com.example.demo.service.ICategoryService;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.IEmployeeTrainingHistoryService;

@Service("empserv")
public class EmployeeServImpl implements IEmployeeService {

	private final EmployeeRepository emprepo;
	private final DesignationRepository desigrepo;
	private final DepartmentRepository deptrepo;
	private final TrainingRepository trainRepository;
	private final CompanyRepository comprepo;
	private final EmployeeTrainingHistoryRepository emptrainhistrepo;
	private final IEmployeeTrainingHistoryService emptrainhistserv;
	private final ICategoryService categoryserv;

	public EmployeeServImpl(EmployeeRepository emprepo, DesignationRepository desigrepo, DepartmentRepository deptrepo,
			TrainingRepository trainRepository, CompanyRepository comprepo,
			EmployeeTrainingHistoryRepository emptrainhistrepo, IEmployeeTrainingHistoryService emptrainhistserv,
			ICategoryService categoryserv) {
		super();
		this.emprepo = emprepo;
		this.desigrepo = desigrepo;
		this.deptrepo = deptrepo;
		this.trainRepository = trainRepository;
		this.comprepo = comprepo;
		this.emptrainhistrepo = emptrainhistrepo;
		this.emptrainhistserv = emptrainhistserv;
		this.categoryserv = categoryserv;
	}

	private Logger logger = LoggerFactory.getLogger(getClass());

	private DateTimeFormatter dday = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private DateTimeFormatter ttime = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public Employee saveEmployee(Employee emp) {

//		List<Long> trainingList = emp.getTraining_ids();
		logger.info("Employee Object {} ",emp);
		Employee savedEmployee = emprepo.save(emp);
		if (savedEmployee != null) {
			return savedEmployee;
		} else {
			throw new GlobalException("Employee " + emp.getEmp_name() + " is not saved and No trainings are provided");
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

		int result = emprepo.updateEmployee(emp.getEmp_id(), emp.getEmp_name(), emp.getEmp_code(),emp.getContractor_name(),
				emp.getCategory().getCategory_id(),emp.getJoining_date(), emp.getDepartment().getDept_id(), emp.getDesignation().getDesig_id());

		if (result > 0) { 
			return result;
		} else {
			throw new ResourceNotModifiedException("Employee " + emp.getEmp_name() + " is not updated");
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
	public void uploadEmployeeList(MultipartFile empListExcel) {

		  try (InputStream is = empListExcel.getInputStream();
				  Workbook workbook = new XSSFWorkbook(is)) {
			  Sheet sheet = workbook.getSheetAt(0);
			List<Employee> empList = new ArrayList<>();

			  for (int i = 1; i <= sheet.getLastRowNum(); i++) { // start from row 1 (skip header)
				System.err.println("i= "+i);
				Row row = sheet.getRow(i);
                if (row == null) {System.err.println("Row is NULL"); continue; }
                else {
                	System.err.println("Row is not null");
                	System.err.println("name is "+row.getCell(1).getStringCellValue());
                }

//				System.err.println("name is "+row.getCell(1).getStringCellValue());
//				Employee employee = new Employee();
//				employee.setEmp_name(row.getCell(1).getStringCellValue());
//
//				String dname = row.getCell(2).getStringCellValue();
//				String dept_name = row.getCell(3).getStringCellValue();
//				String compname = row.getCell(4).getStringCellValue();
//				String joining_date = row.getCell(5).getStringCellValue();
//				String contractor_name = row.getCell(6).getStringCellValue();
//				String category = row.getCell(7).getStringCellValue();
//
//				Designation desigObj = desigrepo.findByDesig_name(dname);
//				Category category_object = categoryserv.getCategoryByCategoryName(category);
//				
//				if(category_object!=null) {
//					employee.setCategory(category_object);
//				}
//				else {
//					employee.setCategory(new Category(""));
//				}		 
//
//				Company comp = comprepo.findByComp_name(compname);
//
//				Department dept = null;
//				if (comp != null) {
//					dept = deptrepo.getDepartmentByDeptNameAndCompName(dept_name, comp.getComp_name());
//				} else {
//					dept = deptrepo.getDepartmentByDeptNameAndCompName(dept_name, "");
//				}
//
//				employee.setJoining_date(joining_date);
//				employee.setContractor_name(contractor_name);
//				
//				dept.setCompany(comp);
//				employee.setDepartment(dept);
//				employee.setDesignation(desigObj);
//				logger.info("Employee onject is ",employee);
//				empList.add(employee);
			}
			logger.info("All EMPLOYEES LIST {} ",empList);
			emprepo.saveAll(empList);
		} catch (Exception e) {
			throw new RuntimeException("FILE IS NOT UPLOADED");
		}

	}

}
