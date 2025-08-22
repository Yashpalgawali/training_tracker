package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.EmployeeTrainingDto;
import com.example.demo.entity.CompetencyScore;
import com.example.demo.entity.EmployeeTraining;
import com.example.demo.entity.Training;

public interface IEmployeeTrainingService {

	public int saveEmployeeTraining(EmployeeTraining emp_training);
	
	public List<EmployeeTraining> getAllEmployeesTrainingHistory();
	
	public List<EmployeeTraining> getEmployeesTrainingByEmployeeId(Long empid);
	
	public EmployeeTraining getEmployeesTrainingByEmployeeIdAndTrainingId(Long empid,Long training_id);
	
	public int updateCompletionTime(Long id,String completion_date);
	
	public int updateTrainingTimeAndCompetency(Long id,String training_date,Long competency_id,Long training_time_slot_id);
	
	public Training getTrainingByHistId(Long histid);
	
	public EmployeeTraining getEmployeeTrainingByID(Long emptrainhist_id);
	
	public List<EmployeeTrainingDto> getAllTrainingListOfAllEmployees();
	
	public List<CompetencyScore> getAllTrainingCompetenciesByEmpId(Long emp_id);
	
	public int updateEmployeeTraining(EmployeeTraining emptraining);
}
