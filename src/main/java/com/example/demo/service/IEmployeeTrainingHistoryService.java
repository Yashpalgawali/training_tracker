package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.EmployeeTrainingHistory;

public interface IEmployeeTrainingHistoryService {

	public EmployeeTrainingHistory saveEmployeeTrainingHistory(EmployeeTrainingHistory emptrainhist);
	
	public List<EmployeeTrainingHistory> getAllEmployeeTrainingHistoryByEmployeeId(Long empid);
	
	public long getCountOfTrainingsByTrainId(Long train_id);
	
	public long getCountOfTrainingsByTrainingIdAndEmployeeId(Long train_id,Long emp_id);
	
	public List<EmployeeTrainingHistory> getEmployeeTrainingHistoryByEmployeeIdAndTrainingId(Long empid,Long tid);
	
}
