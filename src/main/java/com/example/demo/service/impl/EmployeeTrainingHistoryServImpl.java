package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.repository.EmployeeTrainingHistoryRepository;
import com.example.demo.service.IEmployeeTrainingHistoryService;

@Service("emptrainhistserv")
public class EmployeeTrainingHistoryServImpl implements IEmployeeTrainingHistoryService {

	private final EmployeeTrainingHistoryRepository emptrainhistrepo;

	public EmployeeTrainingHistoryServImpl(EmployeeTrainingHistoryRepository emptrainhistrepo) {
		super();
		this.emptrainhistrepo = emptrainhistrepo;
	}

	@Override
	public EmployeeTrainingHistory saveEmployeeTrainingHistory(EmployeeTrainingHistory emptrainhist) {
		return emptrainhistrepo.save(emptrainhist);
		
	}

	@Override
	public List<EmployeeTrainingHistory> getAllEmployeeTrainingHistoryByEmployeeId(Long empid) {
		
		return emptrainhistrepo.getAllTrainingHistoryByEmployeeId(empid);
	}
} 
