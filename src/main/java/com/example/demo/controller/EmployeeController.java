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

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.Employee;
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
	public ResponseEntity<ResponseDto> saveEmployee(@RequestBody EmployeeDTO empdto) {
		
		Employee employee = EmployeeMapper.EmployeeDtoToEmployee(empdto, new Employee());
		//employee.setTraining(empdto.getTrainingIds());
		empserv.saveEmployee(employee);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED.toString(), "Employee "+employee.getEmp_name()+" is saved successfully"));
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		
		var emp = empserv.getAllEmployees();
		logger.info("EMPLIST {} ",emp);
		return ResponseEntity.status(HttpStatus.OK).body(emp);
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
		
		empserv.updateEmployee(employee);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(), "Employee "+employee.getEmp_name()+" is UPDATED successfully"));
	}
	
}
