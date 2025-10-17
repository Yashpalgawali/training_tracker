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
import com.example.demo.entity.Designation;
import com.example.demo.service.IDesignationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("designation")
@Tag(name = "Designation Controller",description = "This controller handles endpoints to perform operations like Save Designation,Find, Update the designation")
public class DesignationController {

	private final IDesignationService desigserv;

	/**
	 * @param desigserv
	 */
	public DesignationController(IDesignationService desigserv) {
		super(); 
		this.desigserv = desigserv;
	}
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@PostMapping("/" )
	@Operation(summary = "Save Designation", description = "This endpoint saves the Designation object to the database" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "201" ,description = "The designation is saved Successfully "),
					@ApiResponse(responseCode = "500" ,description = "The designation is NOT Saved ")
			})
	public ResponseEntity<ResponseDto> saveDesignation(@RequestBody Designation designation) {
		Designation savedDesignation = desigserv.saveDesignation(designation);
		return ResponseEntity.status(HttpStatus.CREATED)
							 .body(new ResponseDto(HttpStatus.CREATED.toString(),"Designation "+savedDesignation.getDesigName()+" is saved Successfully"));
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Find Designation By ID", description = "This endpoint finds the Designation object from the database by its ID" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200" ,description = "The designation is found Successfully "),
					@ApiResponse(responseCode = "404" ,description = "The designation is NOT FOUND")
			})
	public ResponseEntity<Designation> getDesignationById(@PathVariable("id") Long compId) {
		var designation = desigserv.getDesignationById(compId);
		return ResponseEntity.status(HttpStatus.OK).body(designation);
	}
	
	@GetMapping("/")
	@Operation(summary = "Find List of Designations", description = "This endpoint finds the List of Designations from the database" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200" ,description = "The Designations are found Successfully "),
					@ApiResponse(responseCode = "404" ,description = "No Designations are found")
			})
	public ResponseEntity<List<Designation>> getAllDesignations() {
		var designation = desigserv.getAllDesignations();
		return ResponseEntity.status(HttpStatus.OK).body(designation);
	}
	
	
	@PutMapping("/")
	@Operation(summary = "Update Designation", description = "This endpoint UPDATES the Designation object to the database" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200" ,description = "The designation is updated Successfully "),
					@ApiResponse(responseCode = "304" ,description = "The designation is NOT updated ")
			})
	public ResponseEntity<ResponseDto> updateDesignation(@RequestBody Designation designation) {
		var updatedDesignation = desigserv.saveDesignation(designation);
		return ResponseEntity.status(HttpStatus.OK)
							 .body(new ResponseDto(HttpStatus.OK.toString(),"Designation "+updatedDesignation.getDesigName()+" is Updated Successfully"));
	}
}
