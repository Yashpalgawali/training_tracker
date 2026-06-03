package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDto;
import com.example.demo.dto.TrainingScheduleDto;
import com.example.demo.entity.TrainingSchedule;
import com.example.demo.export.ExportAllTrainingSchedule;
import com.example.demo.service.ITrainingScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("trainingschedule")
@RequiredArgsConstructor
@Tag(name = "Training Schedule Controller", description = "This controller handles endpoints to perform operations like Find the Training Schedule")
public class TrainingScheduleController {

	private final ITrainingScheduleService trainingscheduleserv;

	@GetMapping("/")
	@Operation(summary = "Find List of Training Schedule", description = "This endpoint finds the List of Training Schedule Schedulefrom the database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The Training Schedule Scheduleare found Successfully "),
			@ApiResponse(responseCode = "404", description = "No Training Schedule Scheduleare found") })
	public ResponseEntity<List<TrainingSchedule>> getAllTrainingSchdules() {
		var trainingList = trainingscheduleserv.getAllTrainingSchedules();
		return ResponseEntity.status(HttpStatus.OK).body(trainingList);
	}

	@PostMapping("/")
	@Operation(summary = "Save the Training Schedule", description = "This endpoint saved the Training Schedule in the Database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The Training Schedule is saved Successfully "),
			@ApiResponse(responseCode = "404", description = "No Training Schedule Scheduleare found") })
	public ResponseEntity<ResponseDto> saveTraining(@RequestBody TrainingScheduleDto trainingSchedule) {
		trainingscheduleserv.saveTrainingSchedule(trainingSchedule);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.CREATED.toString(),
				"Training Scheduled dated on " + trainingSchedule.getTrainingScheduleDate()));
	}
//	
//	@GetMapping("/{id}")
//	@Operation(summary = "Find the Training Schedule Scheduleusing ID", description = "This endpoint finds the Training Schedule Schedulefrom the database by using its ID")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Training Schedule Scheduleis found Successfully "),
//			@ApiResponse(responseCode = "404", description = "No Training Schedule Scheduleis found") })
//	public ResponseEntity<Training Schedule> getTrainingById(@PathVariable Long id) {
//		var training = trainingscheduleserv.getTrainingById(id);
//		return ResponseEntity.status(HttpStatus.OK).body(training );
//	}

	@GetMapping("/year/{year}")
	@Operation(summary = "Find the Training Schedule using Year", description = "This endpoint finds the Training Schedule from the database by using Year")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Training Schedule is found  "),
			@ApiResponse(responseCode = "404", description = "No Training Schedule found for the given year") })
	public ResponseEntity<List<TrainingSchedule>> getTrainingScheduleByYear(@PathVariable String year) {
		var training = trainingscheduleserv.getTrainingScheduleByYear(year);
		return ResponseEntity.status(HttpStatus.OK).body(training);
	}

	@GetMapping("/training/{training_id}")
	@Operation(summary = "Find the Training Schedule using Year", description = "This endpoint finds the Training Schedule from the database by using Year")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Training Schedule is found  "),
			@ApiResponse(responseCode = "404", description = "No Training Schedule found for the given year") })
	public ResponseEntity<List<TrainingSchedule>> getTrainingScheduleByTrainingId(@PathVariable Long training_id) {
		var training = trainingscheduleserv.getTrainingScheduleByTrainingId(training_id);
		return ResponseEntity.status(HttpStatus.OK).body(training);
	}

	@GetMapping("/download/{year}")
	@Operation(summary = "Export the Testing Schedule using Year", description = "This endpoint exports the Testing Schedule from the database by using Year")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Testing Schedule is found  "),
			@ApiResponse(responseCode = "404", description = "No Testing Schedule is found for the year") })
	public ResponseEntity<InputStreamResource> exportTestingScheduleByYear(HttpServletResponse response,
			@PathVariable String year) throws IOException {
		// Set headers
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Training_Schedule_List_" + year + ".xlsx");

		List<TrainingSchedule> alist = trainingscheduleserv.getTrainingScheduleByYear(year);

		ExportAllTrainingSchedule excelExporter = new ExportAllTrainingSchedule(alist);
		byte[] excelContent = excelExporter.export(response);

		// Return the file as a ResponseEntity
		return ResponseEntity.ok().headers(headers)
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(new InputStreamResource(new ByteArrayInputStream(excelContent)));
	}
}
