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

import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.Company;
import com.example.demo.service.ICompanyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("company")
@CrossOrigin("*")
@Tag(name = "Company Controller",description = "This controller handles endpoints to perform operations like Save Company,Find, Update the company")
public class CompanyController {

	private final ICompanyService compserv;

	/**
	 * @param compserv
	 */
	public CompanyController(ICompanyService compserv) {
		super(); 
		this.compserv = compserv;
	}
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@PostMapping("/")
 
	@Operation(summary = "Save Company", description = "This endpoint saves the Company object to the database" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "201" ,description = "The company is saved Successfully "),
					@ApiResponse(responseCode = "500" ,description = "The company is NOT Saved ")
			})
	public ResponseEntity<ResponseDto> saveCompany(@org.springframework.web.bind.annotation.RequestBody Company company) {
		Company savedCompany = compserv.saveCompany(company);
		return ResponseEntity.status(HttpStatus.CREATED)
							 .body(new ResponseDto(HttpStatus.CREATED.toString(),"Company "+savedCompany.getComp_name()+" is saved Successfully"));
	}
	
	@GetMapping("/{id}")
	 
	@Operation(summary = "Find Company By ID", description = "This endpoint finds the Company object from the database by its ID" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200" ,description = "The company is found Successfully "),
					@ApiResponse(responseCode = "404" ,description = "The company is NOT FOUND")
			})
	public ResponseEntity<Company> getCompanyById(@PathVariable("id") Long compId) {
		var company = compserv.getCompanyById(compId);
		return ResponseEntity.status(HttpStatus.OK).body(company);
	}
	
	@GetMapping("/")
	 
	@Operation(summary = "Find List of Companies", description = "This endpoint finds the List of Companies from the database" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200" ,description = "The companies are found Successfully "),
					@ApiResponse(responseCode = "404" ,description = "No companies are found")
			})
	public ResponseEntity<List<Company>> getAllCompanies() {
		var company = compserv.getAllCompanies();
		return ResponseEntity.status(HttpStatus.OK).body(company);
	}
	
	
	@PutMapping("/")
	@Operation(summary = "Update Company", description = "This endpoint UPDATES the Company object to the database" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200" ,description = "The company is updated Successfully "),
					@ApiResponse(responseCode = "304" ,description = "The company is NOT updated ")
			})
	public ResponseEntity<ResponseDto> updateCompany(@RequestBody Company company) {
		var updatedCompany = compserv.saveCompany(company);
		return ResponseEntity.status(HttpStatus.OK)
							 .body(new ResponseDto(HttpStatus.OK.toString(),"Company "+updatedCompany.getComp_name()+" is Updated Successfully"));
	}
}
