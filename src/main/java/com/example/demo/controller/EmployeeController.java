package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Training;
import com.example.demo.mapper.EmployeeMapper;
import com.example.demo.service.IEmployeeService;

@RestController
@RequestMapping("employee")
@CrossOrigin("*")
public class EmployeeController {

	private final IEmployeeService empserv;

	public EmployeeController(IEmployeeService empserv) {
		super();
		this.empserv = empserv;
	}
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@PostMapping("/")
	public ResponseEntity<ResponseDto> saveEmployee(@RequestBody Employee empdto) {
		 
		empserv.saveEmployee(empdto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED.toString(), "Employee "+empdto.getEmp_name()+" is saved successfully"));
	}
	
	@GetMapping("/")
	public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
		
		var emp = empserv.getAllEmployees();
		
		StringBuilder sb = new StringBuilder();
		StringJoiner sj = new StringJoiner(",");
		
//		List<EmployeeDTO> empDtoList = emp.stream().map(employee -> {
//			EmployeeDTO empdto = new EmployeeDTO();
//			empdto.setEmp_id(employee.getEmp_id());
//			empdto.setEmp_name(employee.getEmp_name());
//			empdto.setEmp_code(employee.getEmp_code());
//			empdto.setJoining_date(employee.getJoining_date());
//			empdto.setDesignation(employee.getDesignation().getDesig_name());
//			empdto.setDepartment(employee.getDepartment().getDept_name());
//			empdto.setCompany(employee.getDepartment().getCompany().getComp_name());
//			empdto.setTrainings( employee.getTraining().stream().map(Training::getTraining_name).collect(Collectors.joining(","))); 
//			
//			 
//			return empdto;
//		}).collect(Collectors.toList());
		
		
//		return ResponseEntity.status(HttpStatus.OK).body(empDtoList);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	} 

	@GetMapping("/{id}")
	public ResponseEntity<Employee> getEmployeebyEmployeeId(@PathVariable("id") Long empid) {
		
		var emp = empserv.getEmployeeByEmployeeId(empid);
		return ResponseEntity.status(HttpStatus.OK).body(emp);
	}
	
	@GetMapping("/code/{empcode}")
	public ResponseEntity<Employee> getEmployeebyEmployeeCode(@PathVariable String empcode) {
		
		var emp = empserv.getEmployeeByEmployeeCode(empcode);
		return ResponseEntity.status(HttpStatus.OK).body(emp);
	}
	
	@PutMapping("/")
	public ResponseEntity<ResponseDto> updateEmployee(@RequestBody Employee employee) {
		logger.info("Employee Object to be updated {} ",employee);
		empserv.updateEmployee(employee);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(), "Employee "+employee.getEmp_name()+" is UPDATED successfully"));
	}
	
	@GetMapping("/training/employee/{empid}")
	public ResponseEntity<List<Training>> getAllTrainingsByEmployeeId(@PathVariable Long empid) {
		
		List<Training> trainList = empserv.getAllTrainingsByEmployeeId(empid);
		return ResponseEntity.status(HttpStatus.OK).body(trainList);
				
	}
	
}
