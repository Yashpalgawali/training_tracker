package com.example.demo.entity;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tbl_employee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {

	@Id
	@SequenceGenerator(name = "emp_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "emp_seq")
	@Column(name= "emp_id")
	Long empId;

	@Column(unique = true,name ="emp_code")
	String empCode;

	@Column(name="emp_name")
	String empName;

	@Column(name="joining_date")
	String joiningDate;

	@Column(name="contractor_name")
	String contractorName;

	@Transient
	List<Long> training_ids;

	@ManyToOne
	@JoinColumn(name = "dept_id")
	Department department;

	@ManyToOne
	@JoinColumn(name = "desig_id")
	Designation designation;
 	
	@Transient
	List<String> training_name;
	
	@ManyToOne
	@JoinColumn(name= "category_id")
	Category category;

	int status;
} 
