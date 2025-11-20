package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.EmployeeHistory;
import com.example.demo.service.IEmployeeHistoryService;

@RestController
@RequestMapping("history")
public class EmployeeHistoryController {

	private final IEmployeeHistoryService emphistserv;

	public EmployeeHistoryController(IEmployeeHistoryService emphistserv) {
		super();
		this.emphistserv = emphistserv;
	}
	
	@GetMapping("/employee/{empid}")
	public ResponseEntity<List<EmployeeHistory>> getEmployeeHistoryByEmployeeId(@PathVariable Long empid) {
		
		List<EmployeeHistory> empHistList = emphistserv.getEmployeeHistoryByEmployeeId(empid);
		return ResponseEntity.status(HttpStatus.OK).body(empHistList);
	}
	
	@GetMapping("/employee/code/{empcode}")
	public ResponseEntity<List<EmployeeHistory>> getEmployeeHistoryByEmployeeCode(@PathVariable String empcode) {
		
		List<EmployeeHistory> empHistList = emphistserv.getEmployeeHistoryByEmployeeCode(empcode);
		return ResponseEntity.status(HttpStatus.OK).body(empHistList);
	}
}
