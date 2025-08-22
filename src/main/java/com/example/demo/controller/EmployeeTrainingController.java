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

import com.example.demo.dto.EmployeeTrainingDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.CompetencyScore;
import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeTraining;
import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.entity.Training;
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
	private final IEmployeeService empserv;
	private final IEmployeeTrainingHistoryService emptrainhistserv;

	public EmployeeTrainingController(IEmployeeTrainingService emptrainserv, IEmployeeService empserv,
			IEmployeeTrainingHistoryService emptrainhistserv) {
		super();
		this.emptrainserv = emptrainserv;
		this.empserv = empserv;
		this.emptrainhistserv = emptrainhistserv;
	}

	@GetMapping("/")
	public ResponseEntity<List<EmployeeTraining>> getAllEmployeesTrainingList() {

		List<EmployeeTraining> trainingHistory = emptrainserv.getAllEmployeesTrainingHistory();
		return ResponseEntity.status(HttpStatus.OK).body(trainingHistory);
	}

	private Logger logger = LoggerFactory.getLogger(getClass());

	@GetMapping("/{id}")
	public ResponseEntity<List<EmployeeTraining>> getAllTrainingListByEmployeeId(@PathVariable Long id) {

		List<EmployeeTraining> trainingHistory = emptrainserv.getEmployeesTrainingByEmployeeId(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(trainingHistory);
	}
	
	
	@GetMapping("/{id}/training/{tid}")
	public ResponseEntity<EmployeeTraining> getAllTrainingsByEmployeeIdAndTrainingId(@PathVariable("id") Long empid,@PathVariable("tid") Long trainingId) {

		EmployeeTraining trainingHistory = emptrainserv.getEmployeesTrainingByEmployeeIdAndTrainingId(empid, trainingId);
		System.err.println("Employee Traing found for empid "+empid+" and training id "+trainingId+" is "+trainingHistory.toString() );
		return ResponseEntity.status(HttpStatus.OK).body(trainingHistory);
	}

	@PostMapping("/")
	public ResponseEntity<ResponseDto> saveEmployeeTraining(@RequestBody EmployeeTraining emptraining) {

		emptrainserv.saveEmployeeTraining(emptraining);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(HttpStatus.CREATED.toString(), "Training is started of the Employee"));
	}

	@PutMapping("/")
	public ResponseEntity<ResponseDto> updateEmployeeTraining(@RequestBody EmployeeTraining emptraining) {

		System.err.println("in Employee Training controller " + emptraining.toString());

		emptrainserv.updateEmployeeTraining(emptraining);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(),
				"Training is Updated of the Employee " + emptraining.getEmployee().getEmp_name()));
	}

//	@PatchMapping("/training/{id}")
//	public ResponseEntity<ResponseDto> updateCompletionTime(@PathVariable Long id,
//			@RequestBody Map<String, String> body) {
//		EmployeeTraining employeeTrainingHistory = emptrainserv.getEmployeeTrainingByID(id);
//
//		Employee employee = empserv.getEmployeeByEmployeeId(employeeTrainingHistory.getEmployee().getEmp_id());
//
//		String comp_date = body.get("completion_date");
//		emptrainserv.updateCompletionTime(id, comp_date);
//		Training training = emptrainserv.getTrainingByHistId(id);
//		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(),
//				"Training " + training.getTraining_name() + " is completed Successfully of " + employee.getEmp_name()));
//	}

	@PatchMapping("/training/{id}")
	public ResponseEntity<ResponseDto> updateCompletionTime(@PathVariable Long id,
			@RequestBody Map<String, String> body) {
		EmployeeTraining employeeTrainingHistory = emptrainserv.getEmployeeTrainingByID(id);

		Employee employee = empserv.getEmployeeByEmployeeId(employeeTrainingHistory.getEmployee().getEmp_id());
		System.err.println("Body for Patch "+body.toString());
		String training_date =  body.get("training_date");
		Long competency_id =  Long.parseLong(body.get("competency_id"));

		Long training_time_slot_id = Long.parseLong(body.get("training_time_id"));

		emptrainserv.updateTrainingTimeAndCompetency(id, training_date,competency_id,training_time_slot_id);

		Training training = emptrainserv.getTrainingByHistId(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(),
				"Training " + training.getTraining_name() + " is completed Successfully of " + employee.getEmp_name()));
	}

	@GetMapping("/exporttrainingshistory/excel/{id}")
	public ResponseEntity<InputStreamResource> exportToExcel(HttpServletResponse response,
			@PathVariable("id") Long empid) throws IOException {

		List<EmployeeTrainingHistory> alist = emptrainhistserv.getAllEmployeeTrainingHistoryByEmployeeId(empid);

		// Set headers
		HttpHeaders headers = new HttpHeaders();
		String fname = "Training_History_" + alist.get(0).getEmployee().getEmp_name() + ".xlsx";
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
}
