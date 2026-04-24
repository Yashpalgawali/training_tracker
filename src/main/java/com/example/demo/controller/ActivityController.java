package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Activity;
import com.example.demo.service.IActivityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("activities")
@RequiredArgsConstructor
@Tag(name = "Activity Controller", description = "This controller handles endpoints to perform operations like Find the Activities")
public class ActivityController {

	private final IActivityService activityserv; 

	@GetMapping("/")
	@Operation(summary = "Find List of Activities", description = "This endpoint finds the List of Activities from the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Activities are found Successfully "),
			@ApiResponse(responseCode = "404", description = "No Activities are found") })
	public ResponseEntity<List<Activity>> getAllActivities() {
		var Activity = activityserv.getAllActivities();
		return ResponseEntity.status(HttpStatus.OK).body(Activity);
	}
}
