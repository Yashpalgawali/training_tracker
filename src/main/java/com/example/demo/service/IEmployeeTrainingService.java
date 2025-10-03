package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.ChartDto;
import com.example.demo.dto.EmployeeTrainingDto;
import com.example.demo.entity.CompetencyScore;
import com.example.demo.entity.EmployeeTraining;
import com.example.demo.entity.Training;

public interface IEmployeeTrainingService {

	public EmployeeTraining saveEmployeeTraining(EmployeeTraining emp_training);

	public List<EmployeeTraining> getAllEmployeesTrainingHistory();

	public List<EmployeeTraining> getEmployeesTrainingByEmployeeId(Long empid);

	public EmployeeTraining getEmployeesTrainingByEmployeeIdAndTrainingId(Long empid,Long training_id);

	public int updateCompletionTime(Long id,String completion_date);

	public Training getTrainingByEmpTrainId(Long histid);

	public EmployeeTraining getEmployeeTrainingByID(Long emptrainhist_id);

	public List<EmployeeTrainingDto> getAllTrainingListOfAllEmployees();

	public List<CompetencyScore> getAllTrainingCompetenciesByEmpId(Long emp_id);

	public int updateEmployeeTraining(EmployeeTraining emptraining);
	
	
	public List<ChartDto> getAllEmployeesTrainingForCharts();

}