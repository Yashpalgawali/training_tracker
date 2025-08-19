package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.EmployeeTrainingHistory;

public interface IEmployeeTrainingHistoryService {

	public EmployeeTrainingHistory saveEmployeeTrainingHistory(EmployeeTrainingHistory emptrainhist);
	
	public List<EmployeeTrainingHistory> getAllEmployeeTrainingHistoryByEmployeeId(Long empid);
	
}
