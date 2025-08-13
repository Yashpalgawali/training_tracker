package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.Competency;
import com.example.demo.service.ICompetencyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("competency") 
@Tag(name = "Competency Controller",description = "This controller handles endpoints to perform operations like Save Competency , Find the competency")
public class CompetencyController1 {

	private final ICompetencyService competencyserv;

	/**
	 * @param competencyserv
	 */
	public CompetencyController1(ICompetencyService competencyserv) {
		super(); 
		this.competencyserv = competencyserv;
	}
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@PostMapping("/")
 
	@Operation(summary = "Save Competency", description = "This endpoint saves the Competency object to the database" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "201" ,description = "The competency is saved Successfully "),
					@ApiResponse(responseCode = "500" ,description = "The competency is NOT Saved ")
			})
	public ResponseEntity<ResponseDto> saveCompetency(@RequestBody Competency competency) {
		Competency savedCompetency = competencyserv.saveCompetency(competency);
		return ResponseEntity.status(HttpStatus.CREATED)
							 .body(new ResponseDto(HttpStatus.CREATED.toString(),"Competency "+savedCompetency.getScore()+" is saved Successfully"));
	}
	
	 
	
	@GetMapping("/")	 
	@Operation(summary = "Find List of Competencies", description = "This endpoint finds the List of Competencies from the database" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200" ,description = "The companies are found Successfully "),
					@ApiResponse(responseCode = "404" ,description = "No companies are found")
			})
	public ResponseEntity<List<Competency>> getAllCompetencies() {
		var competency = competencyserv.getAllCompetencyList();
		return ResponseEntity.status(HttpStatus.OK).body(competency);
	}
	
	@GetMapping("/{id}")	 
	@Operation(summary = "Find List of Competencies By EMployee", description = "This endpoint finds the List of Competencies from the database" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200" ,description = "The companies are found Successfully "),
					@ApiResponse(responseCode = "404" ,description = "No companies are found")
			})
	public ResponseEntity<List<Competency>> getCompetencyByEmpId(@PathVariable Long id) {
		var competency = competencyserv.getAllCompetenciesbyEmpId(id);
		return ResponseEntity.status(HttpStatus.OK).body(competency);
	}
	
	 
}
