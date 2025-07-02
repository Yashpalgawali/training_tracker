package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	Long emp_id;

	String emp_code;

	String emp_name;

	String joining_date;

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
	
//	@ManyToMany
//	@JoinTable(
//			joinColumns = @JoinColumn(name = "emp_id"),
//			inverseJoinColumns = @JoinColumn(name = "traninig_id")
//		) 
//	private List<Training> training = new ArrayList<>();
//	@OneToMany(mappedBy = "employee")
//	private List<EmployeeTraining> employeeTrainings = new ArrayList<>();
} 
