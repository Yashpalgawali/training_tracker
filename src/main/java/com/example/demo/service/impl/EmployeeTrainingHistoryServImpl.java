package com.example.demo.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EmployeeTrainingHistoryRepository;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.IEmployeeTrainingHistoryService;

@Service("emptrainhistserv")
public class EmployeeTrainingHistoryServImpl implements IEmployeeTrainingHistoryService {

	private final EmployeeTrainingHistoryRepository emptrainhistrepo; 
	private final IEmployeeService empserv;
	
	/**
	 * @param emptrainhistrepo	  
	 */
	public EmployeeTrainingHistoryServImpl(EmployeeTrainingHistoryRepository emptrainhistrepo ,@Lazy IEmployeeService empserv) {
		super();
		this.emptrainhistrepo = emptrainhistrepo;	
		this.empserv= empserv;
	}

	@Override
	public EmployeeTrainingHistory saveEmployeeTrainingHistory(EmployeeTrainingHistory history) {
		EmployeeTrainingHistory savedhistory = emptrainhistrepo.save(history);
		
		if(savedhistory!=null) {
			return savedhistory;
		}
		else {
			throw new GlobalException("No training is assigned to the Employee "+history.getEmployee().getEmp_name());
		}
		
	}

	@Override
	public List<EmployeeTrainingHistory> getAllEmployeesTrainingHistory() {
	 
		return emptrainhistrepo.findAll();
	}

	@Override
	public List<EmployeeTrainingHistory> getEmployeesTrainingHistoryByEmployeeId(Long empid) {
		 Employee emp = empserv.getEmployeeByEmployeeId(empid);
		 
		 List<EmployeeTrainingHistory> empHistList = Optional.ofNullable(
				    emptrainhistrepo.findByEmployeeId(emp.getEmp_id())
				).orElse(Collections.emptyList());
			 
		  return empHistList;

	}

}
