package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Training;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.TrainingRepository;
import com.example.demo.service.ITrainingService;

import lombok.RequiredArgsConstructor;

@Service("trainserv")
@RequiredArgsConstructor
public class TrainingServImpl implements ITrainingService {

	private final TrainingRepository trainrepo;

	@Override
	public void saveTraining(Training training) {

		String trimmedTraining = training.getTraining_name().trim();

		Optional<Training> byTraining_name = trainrepo.findByTraining_name(trimmedTraining);
		if (byTraining_name.isPresent()) {
			throw new ResourceAlreadyExistsException("Training", "Training Name ", trimmedTraining);
		}
		training.setFrequency(training.getFrequency().trim());

		training.setTraining_name(trimmedTraining);
		var savedTraining = trainrepo.save(training);
		if (savedTraining == null) {
			throw new GlobalException("Training " + training.getTraining_name() + " is not saved");
		}
	}

	@Override
	public Training getTrainingById(Long tid) {

		return trainrepo.findById(tid)
				.orElseThrow(() -> new ResourceNotFoundException("No Training found for given ID " + tid));
	}

	@Override
	public List<Training> getAllTrainings() {
		var tlist = trainrepo.findAll();
		if (tlist.size() > 0) {
			return tlist;
		} else {
			throw new ResourceNotFoundException("No Trainings found");
		}
	}

	@Override
	@Transactional
	public void updateTraining(Training training) {
		training.setTraining_name(training.getTraining_name().trim());
		training.setFrequency(training.getFrequency().trim());

		var res = trainrepo.updateTraining(training.getTraining_id(), training.getTraining_name(),
				training.getFrequency());
		if (res < 0)
			throw new ResourceNotFoundException("No Training found for given ID " + training.getTraining_id());
	}

}
