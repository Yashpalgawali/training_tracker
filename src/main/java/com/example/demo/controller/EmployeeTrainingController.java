package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.service.IEmployeeTrainingHistoryService;

@RestController
@RequestMapping("employee-training")
public class EmployeeTrainingController {

	private final IEmployeeTrainingHistoryService emptrainhistserv;

	public EmployeeTrainingController(IEmployeeTrainingHistoryService emptrainhistserv) {
		super();
		this.emptrainhistserv = emptrainhistserv;
	}

	@GetMapping("/")
	public ResponseEntity<List<EmployeeTrainingHistory>> getAllEmployeesTrainingList() {

		List<EmployeeTrainingHistory> trainingHistory = emptrainhistserv.getAllEmployeesTrainingHistory();
		return ResponseEntity.status(HttpStatus.OK).body(trainingHistory);
	}

	@GetMapping("/{id}")
	public ResponseEntity<List<EmployeeTrainingHistory>> getAllTrainingListByEmployeeId(@PathVariable Long id) {
		System.err.println("ID IS " + id);
		List<EmployeeTrainingHistory> trainingHistory = emptrainhistserv.getEmployeesTrainingHistoryByEmployeeId(id);
		trainingHistory.forEach(System.err::println);
		return ResponseEntity.status(HttpStatus.OK).body(trainingHistory);
	}

	@PostMapping("/")
	public ResponseEntity<ResponseDto> saveEmployeeTraining(@RequestBody EmployeeTrainingHistory emptraining) {
		System.err.println("OBNJEt IS " + emptraining.toString());

		emptrainhistserv.saveEmployeeTrainingHistory(emptraining);

		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}

}
