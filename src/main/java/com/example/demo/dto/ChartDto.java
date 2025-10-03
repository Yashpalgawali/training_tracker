package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ChartDto {

	private String training_name;
	
	private Integer total_emp;
	
	private Integer comp25;
	
	private Integer comp50;
	
	private Integer comp75;
	
	private Integer comp100;
	
}