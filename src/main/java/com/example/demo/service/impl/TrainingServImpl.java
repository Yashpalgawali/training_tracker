package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Training;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.TrainingRepository;
import com.example.demo.service.ITrainingService;

@Service("trainserv")
public class TrainingServImpl implements ITrainingService {

	private final TrainingRepository trainrepo;
	
	/**
	 * @param trainrepo
	 */
	public TrainingServImpl(TrainingRepository trainrepo) {
		super();
		this.trainrepo = trainrepo;
	}

	@Override
	public Training saveTraining(Training training) {
		var savedTraining = trainrepo.save(training);
		if(savedTraining!=null) {
			return savedTraining;
		}
		else {
			throw new GlobalException("Training "+training.getTraining_name()+" is not saved");
		}		
	}

	@Override
	public Training getTrainingById(Long tid) {
	 
		return trainrepo.findById(tid).orElseThrow(()-> new ResourceNotFoundException("No Traininig found for given ID "+tid) );
	}

	@Override
	public List<Training> getAllTrainings() {
		var tlist = trainrepo.findAll();
		if(tlist.size()>0) {
			return tlist;
		}
		else {
			throw new ResourceNotFoundException("No Trainings found");
		}
	}

	@Override
	public int updateTraining(Training training) {
		var res = trainrepo.updateTraining(training.getTraining_id(),training.getTraining_name());
		if(res>0) {
			return res;
		}
		else {
			throw new ResourceNotFoundException("No Training found for given ID "+training.getTraining_id());
		}
	}
	
}
