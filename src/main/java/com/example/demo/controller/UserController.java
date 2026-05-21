package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.Users;
import com.example.demo.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "Users Controller", description = "This controller handles endpoints to perform operations like Save Users, Find, Update the users")
public class UserController {

	private final IUserService userserv;

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@PostMapping("/")
	@Operation(summary = "Create User", description = "This endpoint created the User object to the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "The users is created Successfully "),
			@ApiResponse(responseCode = "500", description = "The users is NOT created ") })
	public ResponseEntity<ResponseDto> createUser(@RequestBody Users users) {
		userserv.createUser(users);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED.toString(),
				"User " + users.getUsername() + " is saved Successfully"));
	}

	@GetMapping("/{email}")
	@Operation(summary = "Find Users By Email", description = "This endpoint finds the Users object from the database by its Email")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The users is found Successfully "),
			@ApiResponse(responseCode = "404", description = "The users is NOT FOUND") })
	public ResponseEntity<Users> getUserByEmailId(@PathVariable String email) {
		var users = userserv.getUserByEmail(email);
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}

	@GetMapping("/")
	@Operation(summary = "Find List of Users", description = "This endpoint finds the List of Users from the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The Users are found Successfully "),
			@ApiResponse(responseCode = "404", description = "No Users are found") })
	public ResponseEntity<List<Users>> getAllUsers() {
		var users = userserv.getAllUsers();
		logger.info("All users List {} ",users);
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}

	@PutMapping("/")
	@Operation(summary = "Update User", description = "This endpoint UPDATES the Users object to the database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The users is updated Successfully "),
			@ApiResponse(responseCode = "304", description = "The user is NOT updated ") })
	public ResponseEntity<ResponseDto> updateUser(@RequestBody Users users) {
		userserv.updateUser(users);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(),
				"Users " + users.getUsername()+ " is Updated Successfully"));
	}
	
	@GetMapping("/id/{id}")
	@Operation(summary = "Find User By ID", description = "This endpoint finds the Users object from the database by its ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "The users is found Successfully "),
			@ApiResponse(responseCode = "404", description = "The users is NOT FOUND") })
	public ResponseEntity<Users> getUserById(@PathVariable Long id) {
		var user = userserv.getUserById(id);
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}  
}
