package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.example.demo.service.ICategoryService;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.IEmployeeTrainingService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("employee")
public class EmployeeController {

	private final IEmployeeService empserv;
	private final IEmployeeTrainingService emptrainhistserv;
	
	
public EmployeeController(IEmployeeService empserv, IEmployeeTrainingService emptrainhistserv,
			ICategoryService categoryserv ) {
		super();
		this.empserv = empserv;
		this.emptrainhistserv = emptrainhistserv;
		 
	}
//	/**
//	 * @param empserv
//	 * @param emptrainhistserv
//	 */
//	public EmployeeController(IEmployeeService empserv, IEmployeeTrainingService emptrainhistserv) {
//		super();
//		this.empserv = empserv;
//		this.emptrainhistserv = emptrainhistserv;
//	}

	private Logger logger = LoggerFactory.getLogger(getClass());

	@PostMapping("/")
	public ResponseEntity<ResponseDto> saveEmployee(@RequestBody Employee emp) {
		
		empserv.saveEmployee(emp);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED.toString(),
				"Employee " + emp.getEmpName() + " is saved successfully"));
	}

	@GetMapping("/")
	public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {

		List<Employee> empList = empserv.getAllEmployees();
		List<EmployeeDTO> collect = empList.stream().map(emp -> {
			String training_names = emptrainhistserv.getEmployeesTrainingByEmployeeId(emp.getEmpId()).stream()
					.map(hist -> hist.getTraining().getTraining_name()).filter(Objects::nonNull)
					.collect(Collectors.joining(","));
		
			EmployeeDTO empdto = new EmployeeDTO();

			empdto.setEmp_id(emp.getEmpId());
			empdto.setEmp_name(emp.getEmpName());
			empdto.setEmp_code(emp.getEmpCode());
			empdto.setJoining_date(emp.getJoiningDate());
			empdto.setContractor_name(emp.getContractorName());
			if(emp.getDepartment()!=null)
			{
				empdto.setCompany(emp.getDepartment().getCompany().getComp_name());
				empdto.setDepartment(emp.getDepartment().getDept_name());
			}
			else {
				empdto.setCompany("");
				empdto.setDepartment("");
			}
			if(emp.getDesignation()!=null) {
				empdto.setDesignation(emp.getDesignation().getDesig_name());
			}
			else {
				empdto.setDesignation("");
			}
			 
			empdto.setTrainings(training_names);
			return empdto;

		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(collect);

	}
	
	  // Fetch paginated employees
//    @GetMapping("/paged")
//	public Map<String, Object> getEmployees(
//            @RequestParam(defaultValue = "0") int start,
//            @RequestParam(defaultValue = "10") int length,
//            @RequestParam(name = "search[value]", required = false) String searchValue) {
//    	 
////			Map<String, Object> response = empserv.getAllEmployeesWithPagination(Integer.valueOf(page) ,Integer.valueOf(size));
//    		Map<String, Object> response = empserv.getAllEmployeesWithPagination(start ,length, searchValue);
//	        return response;
//	}

	   @GetMapping("/paged")
		public Map<String, Object> getEmployees(
	            @RequestParam(defaultValue = "0") int start,
	            @RequestParam(defaultValue = "10") int length,
	            @RequestParam(required = false) String search,
	            @RequestParam(required = false) String orderColumn,
	            @RequestParam(required = false) String orderDir ) {
	    	 

	    		Map<String, Object> response = empserv.getAllEmployeesWithPagination(start ,length, search,orderColumn,orderDir);
		        return response;
		}	
	
// 	With pagination
//	@GetMapping("paged/")
//	public ResponseEntity<List<EmployeeDTO>> getAllEmployeesWithPagination(@RequestParam String page, @RequestParam String size) {
//
//		List<Employee> empList = empserv.getAllEmployees();
//		List<EmployeeDTO> collect = empList.stream().map(emp -> {
//			String training_names = emptrainhistserv.getEmployeesTrainingByEmployeeId(emp.getEmp_id()).stream()
//					.map(hist -> hist.getTraining().getTraining_name()).filter(Objects::nonNull)
//					.collect(Collectors.joining(","));
//		
//			EmployeeDTO empdto = new EmployeeDTO();
//
//			empdto.setEmp_id(emp.getEmp_id());
//			empdto.setEmp_name(emp.getEmp_name());
//			empdto.setEmp_code(emp.getEmp_code());
//			empdto.setJoining_date(emp.getJoining_date());
//			empdto.setContractor_name(emp.getContractor_name());
//			if(emp.getDepartment()!=null)
//			{
//				empdto.setCompany(emp.getDepartment().getCompany().getComp_name());
//				empdto.setDepartment(emp.getDepartment().getDept_name());
//			}
//			else {
//				empdto.setCompany("");
//				empdto.setDepartment("");
//			}
//			if(emp.getDesignation()!=null) {
//				empdto.setDesignation(emp.getDesignation().getDesig_name());
//			}
//			else {
//				empdto.setDesignation("");
//			}
//			 
//			empdto.setTrainings(training_names);
//			return empdto;
//
//		}).collect(Collectors.toList());
//
//		return ResponseEntity.status(HttpStatus.OK).body(collect);
//
//	}

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
				"Employee " + employee.getEmpName() + " is UPDATED successfully"));
	}

	@GetMapping("/training/employee/{empid}")
	public ResponseEntity<List<Training>> getAllTrainingsByEmployeeId(@PathVariable Long empid) {

		List<Training> trainList = empserv.getAllTrainingsByEmployeeId(empid);
		return ResponseEntity.status(HttpStatus.OK).body(trainList);

	}
	
	@GetMapping("/export/employee/list")
//	@Operation(description = "This end point will Export the All assigned assets to excel file ", summary ="Export All Assigned Assets to the Excel")
//	@ApiResponse(description = "This will export assigned assets to the Employee ", responseCode = "200" )		
	public ResponseEntity<InputStreamResource> exportToExcel(HttpServletResponse response) throws IOException {
		
		// Set headers
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Employee_List.xlsx");

		List<EmployeeDTO> empDtoList = empserv.getAllEmployees().stream().map(emp -> {			 
		
			EmployeeDTO empdto = new EmployeeDTO();

			empdto.setEmp_id(emp.getEmpId());
			empdto.setEmp_name(emp.getEmpName());
			empdto.setEmp_code(emp.getEmpCode());
			empdto.setJoining_date(emp.getJoiningDate());
			empdto.setContractor_name(emp.getContractorName());
			empdto.setCompany(emp.getDepartment().getCompany().getComp_name());
			empdto.setDepartment(emp.getDepartment().getDept_name());
			
			if(emp.getDesignation()!=null) {
				empdto.setDesignation(emp.getDesignation().getDesig_name());
			}
			else {
				empdto.setDepartment("");
			}
			if(emp.getCategory()==null) {
				empdto.setCategory("");							
			}
			else {
				empdto.setCategory(emp.getCategory().getCategory());
			}
			 
			return empdto;

		}).collect(Collectors.toList());		 
		 
		logger.info("EMPLDTO LIST is {} ",empDtoList);
		
		ExportAllEmployees excelExporter = new ExportAllEmployees(empDtoList);
		byte[] excelContent = excelExporter.export(response);

		// Return the file as a ResponseEntity
		return ResponseEntity.ok().headers(headers)
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(new InputStreamResource(new ByteArrayInputStream(excelContent)));
	}
	
	
	@GetMapping("/export/sample/employeelist")
	public ResponseEntity<InputStreamResource> exportSampleToUploadEmployeesToExcel(HttpServletResponse response) throws IOException {
		
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
	public ResponseEntity<String> uploadEmployeeList(@RequestParam MultipartFile empListExcel) throws IOException {
		
		if(empListExcel.isEmpty())
		{ return ResponseEntity.badRequest().body("Please get a file to upload"); }

		InputStream inputStream = empListExcel.getInputStream();
		empserv.uploadEmployeeList(inputStream);
		
		return ResponseEntity.status(HttpStatus.OK).body("uploaded");
			
	}
	
	
	@GetMapping("/employee/dto")
	public ResponseEntity<List<EmployeeDTO>> getAllEmployeesDTO() {
		
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}

