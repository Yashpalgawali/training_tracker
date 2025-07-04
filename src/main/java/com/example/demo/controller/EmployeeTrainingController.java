package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.entity.Training;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.IEmployeeTrainingHistoryService;

@RestController
@RequestMapping("employee-training")
public class EmployeeTrainingController {

	private final IEmployeeTrainingHistoryService emptrainhistserv;
	private final IEmployeeService empserv;

	public EmployeeTrainingController(IEmployeeTrainingHistoryService emptrainhistserv, IEmployeeService empserv) {
		super();
		this.emptrainhistserv = emptrainhistserv;
		this.empserv = empserv;
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
		emptrainhistserv.saveEmployeeTrainingHistory(emptraining);

		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED.toString(), "Training is started of the Employee"));
	}

	@PatchMapping("/training/{id}")
	public ResponseEntity<ResponseDto> updateCompletionTime(@PathVariable Long id,
			@RequestBody Map<String, String> body) {
		EmployeeTrainingHistory employeeTrainingHistory = emptrainhistserv.getEmployeeTrainingHistoryByID(id);

		System.err.println("HISTORY OBJECT FOUND " + employeeTrainingHistory.toString());
		
		Employee employee = empserv.getEmployeeByEmployeeId(employeeTrainingHistory.getEmployee().getEmp_id());
		
		String comp_date = body.get("completion_date");
		emptrainhistserv.updateCompletionTime(id, comp_date);
		Training training = emptrainhistserv.getTrainingByHistId(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(),
				"Training " + training.getTraining_name() + " is completed Successfully of "+employee.getEmp_name()));
	}
}
