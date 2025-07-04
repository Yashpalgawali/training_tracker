package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.Training;
import com.example.demo.service.ITrainingService;

@RestController
@RequestMapping("training")
@CrossOrigin("*")
public class TrainingController {

	private final ITrainingService trainserv;

	public TrainingController(ITrainingService trainserv) {
		super();
		this.trainserv = trainserv;
	}
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	@PostMapping("/")
	public ResponseEntity<ResponseDto> saveTraining(@RequestBody Training training) {

		trainserv.saveTraining(training);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED.toString(),
				"Training " + training.getTraining_name() + " is saved successfully"));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Training> getTrainingById(@PathVariable("id") Long tid) {

		var train = trainserv.getTrainingById(tid);
		return ResponseEntity.status(HttpStatus.OK).body(train);
	}

	@GetMapping("/")
	public ResponseEntity<List<Training>> getAllTrainings() {

		var train = trainserv.getAllTrainings();
		return ResponseEntity.status(HttpStatus.OK).body(train);
	}

	@PutMapping("/")
	public ResponseEntity<ResponseDto> updateTraining(@RequestBody Training training) {

		trainserv.updateTraining(training);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(),
				"Training " + training.getTraining_name() + " is UPDATED successfully"));
	}
	
	@GetMapping("/empid/{id}")
	public List<Training> getAllTrainingsByEmpId(@PathVariable Long id){
		
		//List<Training> trainList = trainserv.getAllTrainingsByEmpId(id);
		return null;
	}
	
}
