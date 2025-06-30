package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Department;
import com.example.demo.entity.Designation;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Training;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.mapper.EmployeeMapper;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.DesignationRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.TrainingRepository;
import com.example.demo.service.IEmployeeService;

import lombok.AllArgsConstructor;

@Service("empserv")
@AllArgsConstructor
public class EmployeeServImpl implements IEmployeeService {

	private final EmployeeRepository emprepo;
	private final DesignationRepository desigrepo;
	private final DepartmentRepository deptrepo;
	private final TrainingRepository trainRepository;
	
	@Override
	public Employee saveEmployee(EmployeeDTO empdto) {
				 
		System.err.println("EMP "+empdto.toString());
//		 
//		List<String> tidslist = empdto.getTrainingIds();
//		List<Training> tlist = new ArrayList<>();
//		tidslist.stream().map(train->{
//			Training training = new Training();
//			training.setTraining_id(Long.valueOf(train));
//			return training;
//		}).collect(Collectors.toList());
//		
//		Employee employee = EmployeeMapper.EmployeeDtoToEmployee(empdto, new Employee());
//		employee.setTraining(tlist);
//		if(empdto.getTraining_ids()!= null && !empdto.getTraining_ids().isEmpty()) {
//			List<Training> trains = trainRepository.findAllById(empdto.getTraining_ids());
//			employee.setTraining(trains);
//		}
//		
//		var employee = emprepo.save(emp);
//		if (employee != null) {
//			return employee;
//		} else {
//			throw new GlobalException("Employee " + emp.getEmp_name() + " is not saved");
//		}
		
		return null;
	}

	@Override
	public Employee getEmployeeByEmployeeId(Long empid) {

		return emprepo.findById(empid)
				.orElseThrow(() -> new ResourceNotFoundException("No Employee found for given ID " + empid));
	}

	@Override
	public int updateEmployee(Employee emp) {
		Long dept_id = emp.getDepartment().getDept_id();
		Department dept = deptrepo.findById(dept_id).get();
		
		Designation desig = desigrepo.findById(emp.getDesignation().getDesig_id()).get();
//		emp.setDepartment(dept);
//		emp.setDesignation(desig);
		
		int res = emprepo.updateEmployee(emp.getEmp_id(), emp.getEmp_name(), emp.getEmp_code(), emp.getJoining_date(),
				dept.getDept_id(), desig.getDesig_id());
		if (res > 0) {
			return res;
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

}
