package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.service.IEmployeeTrainingHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("employee-training-history") @RequiredArgsConstructor
public class EmployeeTrainingHistoryController {

	private final IEmployeeTrainingHistoryService emptrainhistserv;

	@GetMapping("/employee/{empid}/training/{tid}")
	public ResponseEntity<List<EmployeeTrainingHistory>> getEmployeeTrainingHistoryByEmpAndTrainingId(
			@PathVariable Long empid, @PathVariable Long tid) {

		List<EmployeeTrainingHistory> list = emptrainhistserv.getEmployeeTrainingHistoryByEmployeeIdAndTrainingId(empid,
				tid);

		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/employee/{empid}")
	public ResponseEntity<List<EmployeeTrainingHistory>> getEmployeeTrainingHistoryByEmpId(@PathVariable Long empid) {

		List<EmployeeTrainingHistory> list = emptrainhistserv.getAllEmployeeTrainingHistoryByEmployeeId(empid);

		return ResponseEntity.status(HttpStatus.OK).body(list);
	}

}
