package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Training;
import com.example.demo.export.ExportAllEmployees;
import com.example.demo.export.ExportSampleToUploadEmployees;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.IEmployeeTrainingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("employee") @RequiredArgsConstructor
public class EmployeeController {

	private final IEmployeeService empserv;
	private final IEmployeeTrainingService emptrainhistserv;
 
	private Logger logger = LoggerFactory.getLogger(getClass());

	@PostMapping("/")
	@Operation(summary = "Save Employee", description = "This endpoint saves the Employee data in the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "The Employee is saved Successfully "),
			@ApiResponse(responseCode = "500", description = "The Employee is NOT Saved ") })
	public ResponseEntity<ResponseDto> saveEmployee(@RequestBody Employee emp) {

		empserv.saveEmployee(emp);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED.toString(),
				"Employee " + emp.getEmpName() + " is saved successfully"));
	}

	@GetMapping("/")
	@Operation(summary = "Get All Employees", description = "This endpoint retrieves the List of Employees ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Employee is saved Successfully "),
			@ApiResponse(responseCode = "404", description = "The Employee List is NOT found ") })
	public ResponseEntity<List<EmployeeDTO>> getAllEmployeesDtos() {

		List<Employee> empList = empserv.getAllEmployees();
		List<EmployeeDTO> collect = empList.stream().map(emp -> {
			String training_names = emptrainhistserv.getEmployeesTrainingByEmployeeId(emp.getEmpId()).stream()
					.map(hist -> hist.getTraining().getTraining_name()).filter(Objects::nonNull)
					.collect(Collectors.joining(","));

			EmployeeDTO empdto = new EmployeeDTO();

			if (training_names != "") {
				empdto.setIsTrainingGiven(true);
			} else {
				empdto.setIsTrainingGiven(false);
			}

			empdto.setEmpId(emp.getEmpId());
			empdto.setEmpName(emp.getEmpName());
			empdto.setEmpCode(emp.getEmpCode());
			empdto.setJoiningDate(emp.getJoiningDate());
			empdto.setContractorName(emp.getContractorName());
			if (emp.getDepartment() != null) {
				empdto.setCompany(emp.getDepartment().getCompany().getCompName());
				empdto.setDepartment(emp.getDepartment().getDeptName());
			} else {
				empdto.setCompany("");
				empdto.setDepartment("");
			}
			if (emp.getDesignation() != null) {
				empdto.setDesignation(emp.getDesignation().getDesigName());
			} else {
				empdto.setDesignation("");
			}
			if (emp.getStatus() == 1) {
				empdto.setStatus("Active");
			} else {
				empdto.setStatus("InActive");
			}
			if (emp.getLeaveDate() == null || emp.getLeaveDate().equals("")) {
				empdto.setLeaveDate("");
			} else {
				empdto.setLeaveDate(emp.getLeaveDate());
			}
			empdto.setTrainings(training_names);
			return empdto;

		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(collect);

	}

//	@GetMapping("/training/{training_id}/competency/{competency_id}")
//	public ResponseEntity<List<EmployeeDTO>> getAllEmployeesWithoutTrainingAndCompetency(@PathVariable Long training_id,
//			@PathVariable Long competency_id) {
//		List<EmployeeDTO> empList = empserv.getAllEmployeesWithoudTrainingAndCompetency(training_id, competency_id);
//		return ResponseEntity.status(HttpStatus.OK).body(empList);
//
//	}

	@GetMapping("/training/{training_id}/competency/{competency_id}/trainingdate/{tdate}/timeslot/{timeslot}")
	@Operation(summary = "Get Employees Without Training And Competency", description = "This endpoint retrieves the list of Employees Who have not completed the trainings or have no competency")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The list of trained Employees found"),
			@ApiResponse(responseCode = "404", description = "No Employees found ") })
	public ResponseEntity<List<EmployeeDTO>> getAllEmployeesWithoutTrainingAndCompetency(@PathVariable Long training_id,
			@PathVariable Long competency_id, @PathVariable String tdate, @PathVariable Long timeslot) {
		List<EmployeeDTO> empList = empserv.getAllEmployeesWithoudTrainingAndCompetency(training_id, competency_id,
				tdate, timeslot);
		return ResponseEntity.status(HttpStatus.OK).body(empList);

	}

	@GetMapping("/{id}/trainingdate/{tdate}/timeslot/{timeslot}")
	@Operation( summary = "Check Employee attended the other trainings or not", 
				description = "This endpoint checks the Employee have attended any training on provided date and time" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200" ,description = "The Employee have attended the trainings on given date and time"),
					@ApiResponse(responseCode = "500" ,description = "The Employee have not Attended the Trainings on given Date and Time ")
			})
	public ResponseEntity<Integer> checkEmployeeAttendedTrainingOnDateAndTimeSlot(@PathVariable String tdate,
			@PathVariable Long timeslot, @PathVariable Long id) {
		int count = empserv.checkEmployeeAttendedTrainingOnDateAndTimeSlot(id, timeslot, tdate);
		return ResponseEntity.status(HttpStatus.OK).body(count);

	}

	@GetMapping("/active")
	@Operation( summary = "Check Employee is active or not ", 
				description = "This endpoint checks the Employee is active in the company or left the company" )
	@ApiResponses(
	value = {
			@ApiResponse(responseCode = "200" ,description = "The Employee is Active"),
			@ApiResponse(responseCode = "404" ,description = "The Employee is left")
	})
	public ResponseEntity<List<Employee>> getAllActiveEmployees() {

		return ResponseEntity.status(HttpStatus.OK).body(empserv.getAllActiveEmployees());
	}

	@GetMapping("/paged")
	@Operation( summary = "Get List of Employee in paged manner ", 
				description = "This endpoint retrieves the List of Employees in pagination" )
	@ApiResponses(
			value = {
			@ApiResponse(responseCode = "200" ,description = "The Employee is returned"),
			@ApiResponse(responseCode = "404" ,description = "No Employees are present")
	})
	public Map<String, Object> getEmployees(@RequestParam(defaultValue = "0") int start,
			@RequestParam(defaultValue = "10") int length, @RequestParam(required = false) String search,
			@RequestParam(required = false) String orderColumn, @RequestParam(required = false) String orderDir) {

		String mappedColumn = mapSortColumn(orderColumn);
		Map<String, Object> response = empserv.getAllEmployeesWithPagination(start, length, search, mappedColumn,
				orderDir);
		return response;
	}

	private String mapSortColumn(String col) {
		return switch (col) {
		case "empId" -> "empId";
		case "empCode" -> "empCode";
		case "empName" -> "empName";
		case "joiningDate" -> "joiningDate";
		case "contractorName" -> "contractorName";

		case "department" -> "department.deptName";
		case "designation" -> "designation.desigName";
		case "company" -> "department.company.compName"; // <-- IMPORTANT

		default -> "empId"; // fallback
		};
	}

	@GetMapping("/{id}")
	@Operation( summary = "Retrieve Employee by ID", 
		description = "This endpoint returns the details of the Employee based on ID" )
	@ApiResponses(
		value = {
		@ApiResponse(responseCode = "200" ,description = "The Employee is found"),
		@ApiResponse(responseCode = "404" ,description = "The Employee is not found")
	})
	public ResponseEntity<Employee> getEmployeebyEmployeeId(@PathVariable("id") Long empid) {

		var emp = empserv.getEmployeeByEmployeeId(empid);
		return ResponseEntity.status(HttpStatus.OK).body(emp);
	}

	@GetMapping("/code/{empcode}")
	@Operation( summary = "Retrieve Employee by Employee Code", 
		description = "This endpoint returns the details of the Employee based on Employee Code" )
	@ApiResponses(
		value = {
		@ApiResponse(responseCode = "200" ,description = "The Employee is found"),
		@ApiResponse(responseCode = "404" ,description = "The Employee is not found")
	})
	public ResponseEntity<Employee> getEmployeebyEmployeeCode(@PathVariable String empcode) {

		var emp = empserv.getEmployeeByEmployeeCode(empcode);
		return ResponseEntity.status(HttpStatus.OK).body(emp);
	}

	@PutMapping("/")
	@Operation(summary = "Update Employee", description = "This endpoint updated the Employee data in the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "The Employee is updated Successfully "),
			@ApiResponse(responseCode = "500", description = "The Employee is NOT updated ") })
	public ResponseEntity<ResponseDto> updateEmployee(@RequestBody Employee employee) {
		empserv.updateEmployee(employee);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(),
				"Employee " + employee.getEmpName() + " is UPDATED successfully"));
	}

	@GetMapping("/training/employee/{empid}")
	@Operation( summary = "Get the list of trainings Based Employee ID", 
		description = "This endpoint returns the details of the trainings given to the Employee using Employee ID" )
	@ApiResponses(
		value = {
		@ApiResponse(responseCode = "200" ,description = "The Employee Trainings are found"),
		@ApiResponse(responseCode = "404" ,description = "No Employee Trainings found")
	})
	public ResponseEntity<List<Training>> getAllTrainingsByEmployeeId(@PathVariable Long empid) {

		List<Training> trainList = empserv.getAllTrainingsByEmployeeId(empid);
		return ResponseEntity.status(HttpStatus.OK).body(trainList);

	}

	@GetMapping("/export/employee/list")
	@Operation(summary = "Download Employee List", description = "This endpoint downloads the Employee list in excel format")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Employee list is downloaded Successfully"),
			@ApiResponse(responseCode = "500", description = "The Employee list is NOT downloaded") })
	public ResponseEntity<InputStreamResource> exportToExcel(HttpServletResponse response) throws IOException {

		// Set headers
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Employee_List.xlsx");

		List<EmployeeDTO> empDtoList = empserv.getAllEmployees().stream().map(emp -> {

			EmployeeDTO empdto = new EmployeeDTO();

			empdto.setEmpId(emp.getEmpId());
			empdto.setEmpName(emp.getEmpName());
			empdto.setEmpCode(emp.getEmpCode());
			empdto.setJoiningDate(emp.getJoiningDate());
			empdto.setContractorName(emp.getContractorName());
			if (emp.getDepartment() != null) {
				empdto.setCompany(emp.getDepartment().getCompany().getCompName());
				empdto.setDepartment(emp.getDepartment().getDeptName());
			} else {
				empdto.setCompany("");
				empdto.setDepartment("");
			}
			if (emp.getDesignation() != null) {
				empdto.setDesignation(emp.getDesignation().getDesigName());
			} else {
				empdto.setDepartment("");
			}
			if (emp.getCategory() == null) {
				empdto.setCategory("");
			} else {
				empdto.setCategory(emp.getCategory().getCategory());
			}

			return empdto;

		}).collect(Collectors.toList());

		ExportAllEmployees excelExporter = new ExportAllEmployees(empDtoList);
		byte[] excelContent = excelExporter.export(response);

		// excelExporter.export(response);
		// Return the file as a ResponseEntity
		return ResponseEntity.ok().headers(headers)
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(new InputStreamResource(new ByteArrayInputStream(excelContent)));
	}

	@GetMapping("/export/sample/employeelist")
	@Operation(summary = "Download Employee List", description = "This endpoint downloads the Employee list in excel format")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Employee list is downloaded Successfully"),
			@ApiResponse(responseCode = "500", description = "The Employee list is NOT downloaded") })
	public ResponseEntity<InputStreamResource> exportSampleToUploadEmployeesToExcel(HttpServletResponse response)
			throws IOException {

		// Set headers
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Sample_To_Upload_Employee_List.xlsx");

		ExportSampleToUploadEmployees excelExporter = new ExportSampleToUploadEmployees();
		byte[] excelContent = excelExporter.export(response);

		// Return the file as a ResponseEntity
		return ResponseEntity.ok().headers(headers)
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(new InputStreamResource(new ByteArrayInputStream(excelContent)));
	}

	@PostMapping("/upload")
	@Operation(summary = "Upload Employee List", description = "This endpoint uploads the list of Employees in the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "202", description = "Upload accepted – processing in background"),
			@ApiResponse(responseCode = "400", description = "No file provided or file is empty"),
			@ApiResponse(responseCode = "500", description = "Failed to read the uploaded file") })
	public ResponseEntity<String> uploadEmployeeList(@RequestParam MultipartFile empListExcel) throws IOException {

		if (empListExcel.isEmpty()) {
			return ResponseEntity.badRequest().body("Please select a file to upload");
		}

		// Read bytes BEFORE calling the service.
		// The MultipartFile InputStream is bound to this HTTP request and will be
		// closed once the request ends. The @Async background thread would get a
		// 'Stream closed' error if we passed the raw InputStream.
		byte[] fileBytes = empListExcel.getBytes();

		// Fire-and-forget: returns immediately while processing runs in the background.
		empserv.uploadEmployeeList(fileBytes);

		logger.info("Upload request accepted – file='{}', size={} bytes – processing in background",
				empListExcel.getOriginalFilename(), fileBytes.length);

		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body("Upload accepted. Your file is being processed in the background. " +
					  "Check server logs for progress and completion status.");
	}

	@GetMapping("/employee/dto")
	public ResponseEntity<List<EmployeeDTO>> getAllEmployeesDTO() {

		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
