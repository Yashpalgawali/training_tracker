package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

	Long emp_id;
	String emp_code;
	String emp_name;
	String joining_date;
	String department;
	String designation;
	String company;	
	String contractor_name;	
	String trainings;
	String category;
	String training_date;
	String completion_date;
	Boolean isTrainingGiven;
	List<Long> training_ids = new ArrayList<>();
}
