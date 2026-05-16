package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.TrainingScheduleDto;
import com.example.demo.entity.TrainingSchedule;

public interface ITrainingScheduleService {
	
	public void saveTrainingSchedule(TrainingScheduleDto training);
	
	public void updateTrainingSchedule(TrainingScheduleDto training);
	
	public TrainingSchedule getTrainingScheduleById(Long id);
	
	public List<TrainingSchedule> getTrainingScheduleByYear(String year);
	
	public List<TrainingSchedule> getAllTrainingSchedules();
	
	public List<TrainingSchedule> getTrainingScheduleByTrainingId(Long training_id);
}
