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

	Long empId;
	String empCode;
	String empName;
	String joiningDate;
	String department;
	String designation;
	String company;	
	String contractorName;	
	String trainings;
	String category;
	String trainingDate;
	String completionDate;
	Boolean isTrainingGiven;
	List<Long> training_ids = new ArrayList<>();
}
