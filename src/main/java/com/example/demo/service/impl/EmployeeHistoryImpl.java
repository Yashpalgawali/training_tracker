package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeHistory;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EmployeeHistoryRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.IEmployeeHistoryService;
import com.example.demo.service.IEmployeeService;

@Service("emphistserv")
public class EmployeeHistoryImpl implements IEmployeeHistoryService {

	private final EmployeeHistoryRepository emphistrepo;
	private final EmployeeRepository emprepo;

	public EmployeeHistoryImpl(EmployeeHistoryRepository emphistrepo, EmployeeRepository emprepo) {
		super();
		this.emphistrepo = emphistrepo;
		this.emprepo = emprepo;
	}

	@Override
	public EmployeeHistory saveEmployeeHistory(EmployeeHistory history) {

		return emphistrepo.save(history);
	}

	@Override
	public List<EmployeeHistory> getEmployeeHistoryByEmployeeCode(String empcode) {
		List<EmployeeHistory> empList = emphistrepo.findByEmpCode(empcode);
		return empList;
	}

	@Override
	public List<EmployeeHistory> getEmployeeHistoryByEmployeeId(Long empid) {
		Employee emp = emprepo.findById(empid).get();
		return emphistrepo.findByEmployee(emp);
	}

}
