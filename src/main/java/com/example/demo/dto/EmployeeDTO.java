package com.example.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

	String emp_code;

	String emp_name;

	String joining_date;

	String departmentId;
	String designationId;
	List<String> trainingIds;
}
