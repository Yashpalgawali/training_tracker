package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.example.demo.export.ExportAllTrainings;
import com.example.demo.export.ExportEmployeeTrainingHistory;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.IEmployeeTrainingHistoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;

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
		
		List<EmployeeTrainingHistory> trainingHistory = emptrainhistserv.getEmployeesTrainingHistoryByEmployeeId(id);
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

		Employee employee = empserv.getEmployeeByEmployeeId(employeeTrainingHistory.getEmployee().getEmp_id());
		  
		String comp_date = body.get("completion_date");
		emptrainhistserv.updateCompletionTime(id, comp_date);
		Training training = emptrainhistserv.getTrainingByHistId(id);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(),
				"Training " + training.getTraining_name() + " is completed Successfully of "+employee.getEmp_name()));
	}
	
	@RequestMapping("/exporttrainingshistory/excel/{id}")
	public ResponseEntity<InputStreamResource> exportToExcel(HttpServletResponse response,
			@PathVariable("id") Long empid) throws IOException{

		List<EmployeeTrainingHistory> alist = emptrainhistserv.getEmployeesTrainingHistoryByEmployeeId(empid);
		alist.forEach(System.err::println);
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
	
	@GetMapping("/exportassignedassets/excel")
	@Operation(description = "This end point will Export the All assigned assets to excel file ", summary ="Export All Assigned Assets to the Excel")
	@ApiResponse(description = "This will export assigned assets to the Employee ", responseCode = "200" )		
	public ResponseEntity<InputStreamResource> exportToExcel(HttpServletResponse response) throws IOException {
		// Set headers
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=All_Assigned_Assets.xlsx");

		List<EmployeeTrainingHistory> alist = new ArrayList<EmployeeTrainingHistory>();
		List<Object[]> aslist = assignserv.getAllAssignedassetsGroup();

		if (aslist.size() > 0) {
			aslist.forEach(ast -> {
//				AssignedAssets asts = new AssignedAssets();
//
//				String assets = "", asset_type = "";
//
//				asts.setAssigned_asset_id(Long.valueOf(ast[0].toString()));
//				asts.setAssign_date(ast[1].toString());
//				asts.setAssign_time(ast[2].toString());
//				asts.setAsset_id(Long.valueOf(ast[3].toString()));
//				asts.setEmp_id((Long.valueOf(ast[4].toString())));
//
//				assets = Stream.of(ast[5].toString().split(",")).collect(Collectors.toList()).toString();
//
//				assets = assets.replace("[", "").replace("]", "");
//
//				asts.setAssigned(assets);
//
//				asset_type = Stream.of(ast[6].toString().split(",")).collect(Collectors.toList()).toString();
//				asset_type = asset_type.replace("[", "").replace("]", "");
//
//				asts.setAssigned_types(asset_type);
//
//				Employee emp = new Employee();
//
//				emp.setEmp_name(ast[7].toString());
//				emp.setEmp_email(ast[8].toString());
//				emp.setEmp_contact(ast[9].toString());
//
//				Designation desig = new Designation();
//				desig.setDesig_id((Long.valueOf(ast[10].toString())));
//				desig.setDesig_name(ast[11].toString());
//
//				Department dept = new Department();
//				dept.setDept_id((Long.valueOf(ast[12].toString())));
//				dept.setDept_name(ast[13].toString());
//
//				Company comp = new Company();
//				comp.setComp_id(Long.valueOf(ast[14].toString()));
//				comp.setComp_name(ast[15].toString());
//
//				String mod_num = Stream.of(ast[16].toString().split(","))
//								.collect(Collectors.toList()).toString()
//								.replace("[", "").replace("]", "");
//				//mod_num = mod_num.replace("[", "").replace("]", "");
//
//				asts.setModel_numbers(mod_num);
//				dept.setCompany(comp);
//
//				emp.setDepartment(dept);
//				emp.setDesignation(desig);
//				asts.setEmployee(emp);
//
//				alist.add(asts);
			});
		}

		ExportAllTrainings excelExporter = new ExportAllTrainings(alist);
		byte[] excelContent = excelExporter.export(response);

		// Return the file as a ResponseEntity
		return ResponseEntity.ok().headers(headers)
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(new InputStreamResource(new ByteArrayInputStream(excelContent)));

	}
}
