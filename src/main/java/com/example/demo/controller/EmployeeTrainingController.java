package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ChartDto;
import com.example.demo.dto.EmployeeTrainingDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.Competency;
import com.example.demo.entity.CompetencyScore;
import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeTraining;
import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.entity.TrainingTimeSlot;
import com.example.demo.export.ExportAllTrainings;
import com.example.demo.export.ExportEmployeeTrainingHistory;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.IEmployeeTrainingHistoryService;
import com.example.demo.service.IEmployeeTrainingService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("employee-training")
public class EmployeeTrainingController {

	private final IEmployeeTrainingService emptrainserv;
	private final IEmployeeTrainingHistoryService emptrainhistserv;
	private final IEmployeeService empserv;
	
	public EmployeeTrainingController(IEmployeeTrainingService emptrainserv,
			IEmployeeTrainingHistoryService emptrainhistserv, IEmployeeService empserv) {
		super();
		this.emptrainserv = emptrainserv;
		this.emptrainhistserv = emptrainhistserv;
		this.empserv = empserv;
	}

	@GetMapping("/")
	public ResponseEntity<List<EmployeeTraining>> getAllEmployeesTrainingList() {

		List<EmployeeTraining> trainingHistory = emptrainserv.getAllEmployeesTrainingHistory();
		return ResponseEntity.status(HttpStatus.OK).body(trainingHistory);
	}

//	private Logger logger = LoggerFactory.getLogger(getClass());

	@GetMapping("/{id}")
	public ResponseEntity<List<EmployeeTraining>> getAllTrainingListByEmployeeId(@PathVariable Long id) {

		List<EmployeeTraining> trainingHistory = emptrainserv.getEmployeesTrainingByEmployeeId(id);

		return ResponseEntity.status(HttpStatus.OK).body(trainingHistory);
	}

	@GetMapping("/{id}/training/{tid}")
	public ResponseEntity<EmployeeTraining> getAllTrainingsByEmployeeIdAndTrainingId(@PathVariable("id") Long empid,
			@PathVariable("tid") Long trainingId) {

		System.err.println("Inside get Trainings by employee id " + empid + " and training ID= " + trainingId);
		EmployeeTraining trainingHistory = emptrainserv.getEmployeesTrainingByEmployeeIdAndTrainingId(empid,
				trainingId);

		return ResponseEntity.status(HttpStatus.OK).body(trainingHistory);
	}

	@PostMapping("/")
	public ResponseEntity<ResponseDto> saveEmployeeTraining(@RequestBody EmployeeTraining emptraining) {

		EmployeeTraining savedEmployeeTraining = emptrainserv.saveEmployeeTraining(emptraining);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(HttpStatus.CREATED.toString(), "Training "+savedEmployeeTraining.getTraining().getTraining_name()+" is started of "+savedEmployeeTraining.getEmployee().getEmpName()));
	}

	@PutMapping("/")
	public ResponseEntity<ResponseDto> updateEmployeeTraining(@RequestBody EmployeeTraining emptraining) {

		emptrainserv.updateEmployeeTraining(emptraining);
		
		Employee trainedEmployee = empserv.getEmployeeByEmployeeId(emptraining.getEmployee().getEmpId());
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(),
				"Training is Updated of the Employee " + trainedEmployee.getEmpName() ) );
	}

	@GetMapping("/getalltrainingsforchart")
	public ResponseEntity<List<ChartDto>> getAllEmployeeTrainings() {
		
		var list = emptrainserv.getAllEmployeesTrainingForCharts();
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}


	@PatchMapping("/training/{id}")
	public ResponseEntity<ResponseDto> updateCompletionTime(@PathVariable Long id,
			@RequestBody Map<String, String> body) {
		EmployeeTraining employeeTraining = emptrainserv.getEmployeeTrainingByID(id);

		String training_date = body.get("training_date");
		Long competency_id = Long.parseLong(body.get("competency_id"));

		Long training_time_slot_id = Long.parseLong(body.get("training_time_id"));

		Competency comp = new Competency();
		comp.setCompetency_id(competency_id);

		TrainingTimeSlot trainTimeSlot = new TrainingTimeSlot();

		trainTimeSlot.setTraining_time_slot_id(training_time_slot_id);
		employeeTraining.setTraining_date(training_date);
		employeeTraining.setCompetency(comp);
		employeeTraining.setTrainingTimeSlot(trainTimeSlot);

		emptrainserv.updateEmployeeTraining(employeeTraining);

//		EmployeeTraining afterEmployeeTraining = emptrainserv.getEmployeeTrainingByID(id);
//		System.err.println("After updating the employee training " + afterEmployeeTraining.toString());

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDto(HttpStatus.OK.toString(),
						"Training " + employeeTraining.getTraining().getTraining_name()
								+ " is completed Successfully of " + employeeTraining.getEmployee().getEmpName()));
	}

	@GetMapping("/exporttrainingshistory/excel/{id}")
	public ResponseEntity<InputStreamResource> exportToExcel(HttpServletResponse response,
			@PathVariable("id") Long empid) throws IOException {

		List<EmployeeTrainingHistory> alist = emptrainhistserv.getAllEmployeeTrainingHistoryByEmployeeId(empid);

		// Set headers
		HttpHeaders headers = new HttpHeaders();
		String fname = "Training_History_" + alist.get(0).getEmployee().getEmpName() + ".xlsx";
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fname);

		ExportEmployeeTrainingHistory ahist = new ExportEmployeeTrainingHistory(alist);
		byte[] excelContent = ahist.export(response);

		// Return the file as a ResponseEntity
		return ResponseEntity.ok().headers(headers)
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(new InputStreamResource(new ByteArrayInputStream(excelContent)));
	}

	@GetMapping("/exporttrainings/excel")
//	@Operation(description = "This end point will Export the All assigned assets to excel file ", summary ="Export All Assigned Assets to the Excel")
//	@ApiResponse(description = "This will export assigned assets to the Employee ", responseCode = "200" )		
	public ResponseEntity<InputStreamResource> exportToExcel(HttpServletResponse response) throws IOException {
		// Set headers
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=All_Trainings_List.xlsx");

		List<EmployeeTrainingDto> alist = emptrainserv.getAllTrainingListOfAllEmployees();

		ExportAllTrainings excelExporter = new ExportAllTrainings(alist);
		byte[] excelContent = excelExporter.export(response);

		// Return the file as a ResponseEntity
		return ResponseEntity.ok().headers(headers)
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(new InputStreamResource(new ByteArrayInputStream(excelContent)));

	}

	@GetMapping("/competencies/{id}")
	public ResponseEntity<List<CompetencyScore>> getAllTrainingCompetencyiesByEmployeeId(@PathVariable Long id) {
		List<CompetencyScore> object = emptrainserv.getAllTrainingCompetenciesByEmpId(id);

		return ResponseEntity.status(HttpStatus.OK).body(object);

	}
	
	@GetMapping("/count/{id}")
	public ResponseEntity<Integer> countEmployeesById(@PathVariable Long id) {
		
		int count = emptrainserv.countTrainingByEmpId(id);
		System.err.println("training given count is "+count);
		return ResponseEntity.status(HttpStatus.OK).body(count);
	}
}
