package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Training;

public interface ITrainingService {

	public Training saveTraining(Training training);

	public Training getTrainingById(Long tid);

	public List<Training> getAllTrainings();

	public int updateTraining(Training training);
		
}
