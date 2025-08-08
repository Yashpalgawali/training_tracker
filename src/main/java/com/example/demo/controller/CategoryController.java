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
import com.example.demo.entity.Category;
import com.example.demo.service.ICategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("category")
@Tag(name = "Category Controller",description = "This controller handles endpoints to perform operations like Save Category,Find, Update the category")
public class CategoryController {

	private final ICategoryService categoryserv;
	
	public CategoryController(ICategoryService categoryserv ) {
		super();
		this.categoryserv = categoryserv;
		 
	}


	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@PostMapping("/") 
	@Operation(summary = "Save Category", description = "This endpoint saves the Category object to the database" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "201" ,description = "The category is saved Successfully "),
					@ApiResponse(responseCode = "500" ,description = "The category is NOT Saved ")
			})
	public ResponseEntity<ResponseDto> saveCompany(@RequestBody Category category) {
		Category savedCategory = categoryserv.saveCategory(category);
		return ResponseEntity.status(HttpStatus.CREATED)
							 .body(new ResponseDto(HttpStatus.CREATED.toString(),"Category "+savedCategory.getCategory()+" is saved Successfully"));
	}
	
	@GetMapping("/{id}")
	 
	@Operation(summary = "Find Category By ID", description = "This endpoint finds the Category object from the database by its ID" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200" ,description = "The category is found Successfully "),
					@ApiResponse(responseCode = "404" ,description = "The category is NOT FOUND")
			})
	public ResponseEntity<Category> getCompanyById(@PathVariable("id") Long category_id) {
		var category = categoryserv.getCategoryById(category_id);
		return ResponseEntity.status(HttpStatus.OK).body(category);
	}
	
	@GetMapping("/")	 
	@Operation(summary = "Find List of Categories", description = "This endpoint finds the List of Categories from the database" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200" ,description = "The companies are found Successfully "),
					@ApiResponse(responseCode = "404" ,description = "No companies are found")
			})
	public ResponseEntity<List<Category>> getAllCategories() {
		var category = categoryserv.getAllCategories();
		return ResponseEntity.status(HttpStatus.OK).body(category);
	}
	
	
	@PutMapping("/")
	@Operation(summary = "Update Category", description = "This endpoint UPDATES the Category object to the database" )
	@ApiResponses(
			value = {
					@ApiResponse(responseCode = "200" ,description = "The category is updated Successfully "),
					@ApiResponse(responseCode = "304" ,description = "The category is NOT updated ")
			})
	public ResponseEntity<ResponseDto> updateCompany(@RequestBody Category category) {
		categoryserv.updateCategory(category);
		return ResponseEntity.status(HttpStatus.OK)
							 .body(new ResponseDto(HttpStatus.OK.toString(),"Category "+category.getCategory()+" is Updated Successfully"));
	}
}
