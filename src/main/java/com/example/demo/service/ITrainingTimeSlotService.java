package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.TrainingTimeSlot;

public interface ITrainingTimeSlotService {

	public TrainingTimeSlot saveTrainingTimeSlot(TrainingTimeSlot timeSlot);
	
	public List<TrainingTimeSlot> getAllCompanies();
	
	public TrainingTimeSlot getTrainingTimeSlotById(Long time_slot_id);
	
	public int updateTrainingTimeSlot(TrainingTimeSlot timeSlot);
}
