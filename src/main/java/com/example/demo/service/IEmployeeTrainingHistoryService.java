package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.EmployeeTrainingHistory;

public interface IEmployeeTrainingHistoryService {

	public int saveEmployeeTrainingHistory(EmployeeTrainingHistory history);
	
	public List<EmployeeTrainingHistory> getAllEmployeesTrainingHistory();
	
	public List<EmployeeTrainingHistory> getEmployeesTrainingHistoryByEmployeeId(Long empid);
		
}
