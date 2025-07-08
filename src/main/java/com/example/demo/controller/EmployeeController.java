package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.entity.Training;
import com.example.demo.mapper.EmployeeMapper;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.IEmployeeTrainingHistoryService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("employee")
@CrossOrigin("*")
//@AllArgsConstructor

public class EmployeeController {

	private final IEmployeeService empserv;
	private final IEmployeeTrainingHistoryService emptrainhistserv;

	/**
	 * @param empserv
	 * @param emptrainhistserv
	 */
	public EmployeeController(IEmployeeService empserv, IEmployeeTrainingHistoryService emptrainhistserv) {
		super();
		this.empserv = empserv;
		this.emptrainhistserv = emptrainhistserv;
	}

	private Logger logger = LoggerFactory.getLogger(getClass());

	@PostMapping("/")
	public ResponseEntity<ResponseDto> saveEmployee(@RequestBody Employee empdto) {

		empserv.saveEmployee(empdto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED.toString(),
				"Employee " + empdto.getEmp_name() + " is saved successfully"));
	}

	@GetMapping("/")
	public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {

		List<Employee> empList = empserv.getAllEmployees();

		empList.forEach(System.err::println);

		List<EmployeeDTO> collect = empList.stream().map(emp -> {
			String training_names = emptrainhistserv.getEmployeesTrainingHistoryByEmployeeId(emp.getEmp_id()).stream()
					.map(hist -> hist.getTraining().getTraining_name()).filter(Objects::nonNull)
					.collect(Collectors.joining(","));
		
			EmployeeDTO empdto = new EmployeeDTO();

			empdto.setEmp_id(emp.getEmp_id());
			empdto.setEmp_name(emp.getEmp_name());
			empdto.setEmp_code(emp.getEmp_code());
			empdto.setJoining_date(emp.getJoining_date());
			empdto.setCompany(emp.getDepartment().getCompany().getComp_name());
			empdto.setDepartment(emp.getDepartment().getDept_name());
			empdto.setDesignation(emp.getDesignation().getDesig_name());
			empdto.setTrainings(training_names);
			return empdto;

		}).collect(Collectors.toList());

		collect.stream().forEach(System.err::println);

		return ResponseEntity.status(HttpStatus.OK).body(collect);

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
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(),
				"Employee " + employee.getEmp_name() + " is UPDATED successfully"));
	}

	@GetMapping("/training/employee/{empid}")
	public ResponseEntity<List<Training>> getAllTrainingsByEmployeeId(@PathVariable Long empid) {

		List<Training> trainList = empserv.getAllTrainingsByEmployeeId(empid);
		return ResponseEntity.status(HttpStatus.OK).body(trainList);

	}

}
