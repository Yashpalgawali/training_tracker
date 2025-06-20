package com.example.demo.controller;

import java.util.List;

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

import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.Department;
import com.example.demo.service.IDepartmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("department")
@CrossOrigin("*")
public class DepartmentController {

	private final IDepartmentService deptserv;

	/**
	 * @param deptserv
	 */
	public DepartmentController(IDepartmentService deptserv) {
		super();
		this.deptserv = deptserv;
	}

	@PostMapping("/")
	@Operation(summary = "Save Department", description = "This endpoint is used to save Department into the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "The company is saved successfully"),
			@ApiResponse(responseCode = "500", description = "The company is not saved ") })
	public ResponseEntity<ResponseDto> saveDepartment(@RequestBody Department department) {

		deptserv.saveDepartment(department);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED.toString(),
				"Department " + department.getDept_name() + " is saved successfully"));

	}

	@GetMapping("/{id}")
	@Operation(summary = "Get Department By ID ", description = "This endpoint is used to get the Department from the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The company is found "),
			@ApiResponse(responseCode = "500", description = "The company is not found ") })
	public ResponseEntity<Department> getDepartmentByDepartmentId(@PathVariable("id") Long deptid) {
		var dept = deptserv.getDepartmentByDeptId(deptid);
		return ResponseEntity.status(HttpStatus.OK).body(dept);

	}

	@GetMapping("/")
	@Operation(summary = "Get All Department ", description = "This endpoint is used to get the List of All Departments from the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Departments list is found "),
			@ApiResponse(responseCode = "500", description = "The Departments not found ") })
	public ResponseEntity<List<Department>> getAllDepartment() {
		var dept = deptserv.getAllDepartments();
		return ResponseEntity.status(HttpStatus.OK).body(dept);

	}

	@GetMapping("/company/{id}")
	@Operation(summary = "Get Department By Company ID ", description = "This endpoint is used to get the Departments from the database Based on the Company ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The company is found "),
			@ApiResponse(responseCode = "500", description = "The company is not found ") })
	public ResponseEntity<List<Department>> getDepartmentByCompanyId(@PathVariable("id") Long compid) {
		var dept = deptserv.getAllDepartmentsByCompanyId(compid);
		return ResponseEntity.status(HttpStatus.OK).body(dept);

	}

	@PutMapping("/")
	@Operation(summary = "Update Department", description = "This endpoint is used to Update the Department into the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The company is Updated successfully"),
			@ApiResponse(responseCode = "500", description = "The company is not Updated ") })
	public ResponseEntity<ResponseDto> updateDepartment(@RequestBody Department department) {
		deptserv.updateDepartment(department);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(),
				"Department " + department.getDept_name() + " is Updated successfully"));

	}
}
