package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Training;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.IEmployeeService;

import lombok.AllArgsConstructor;

@Service("empserv")
@AllArgsConstructor
public class EmployeeServImpl implements IEmployeeService {

	private final EmployeeRepository emprepo;

	@Override
	public Employee saveEmployee(Employee emp) {
		
		//System.err.println("EMployee OBJECT "+emp.toString());
		
		String[] tids = emp.getTraining_ids().split(",");
		
		List<Training> tlist = new ArrayList<>();
		
		for(int i=0;i<tids.length;i++)
		{
			Training training = new Training();
			training.setTraining_id(Long.valueOf(tids[i]));
			tlist.add(training);
		}	
		emp.setTraining(tlist);
		System.err.println("EMP "+emp.toString());
		return null;
//		var employee = emprepo.save(emp);
//		if (employee != null) {
//			return employee;
//		} else {
//			throw new GlobalException("Employee " + emp.getEmp_name() + " is not saved");
//		}
	}

	@Override
	public Employee getEmployeeByEmployeeId(Long empid) {

		return emprepo.findById(empid)
				.orElseThrow(() -> new ResourceNotFoundException("No Employee found for given ID " + empid));
	}

	@Override
	public int updateEmployee(Employee emp) {
		int res = emprepo.updateEmployee(emp.getEmp_id(), emp.getEmp_name(), emp.getEmp_code(), emp.getJoining_date(),
				emp.getDepartment().getDept_id(), emp.getDesignation().getDesig_id());
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
			throw new ResourceNotFoundException("No Employees found!!!");
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
