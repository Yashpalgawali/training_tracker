package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.EmployeeTrainingDto;
import com.example.demo.entity.CompetencyScore;
import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.entity.Training;

public interface IEmployeeTrainingHistoryService {

	public int saveEmployeeTrainingHistory(EmployeeTrainingHistory history);
	
	public List<EmployeeTrainingHistory> getAllEmployeesTrainingHistory();
	
	public List<EmployeeTrainingHistory> getEmployeesTrainingHistoryByEmployeeId(Long empid);
	
	public int updateCompletionTime(Long id,String completion_date);
	
	public Training getTrainingByHistId(Long histid);
	
	public EmployeeTrainingHistory getEmployeeTrainingHistoryByID(Long emptrainhist_id);
	
	public List<EmployeeTrainingDto> getAllTrainingListOfAllEmployees();
	
	public List<CompetencyScore> getAllTrainingCompetenciesBuyEmpId(Long emp_id);
}
