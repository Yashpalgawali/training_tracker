package com.example.demo.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeTrainingDto {

	String emp_name;
	String training_name;
	String training_date;
	String completion_date;
	String desig_name;	
	String dept_name;
	String comp_name;
}
