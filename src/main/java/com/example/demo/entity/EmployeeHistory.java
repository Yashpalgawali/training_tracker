package com.example.demo.entity;

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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tbl_employee_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeHistory {

	@Id
	@SequenceGenerator(name = "emp_hist_seq",allocationSize = 1,initialValue = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "emp_hist_seq")
	@Column(name = "emp_hist_id")
	Long empHistId;

	@ManyToOne
	@JoinColumn(name = "emp_id")
	Employee employee;
	
	@Column(name="dept_name")
	String deptName;
	
	@Column(name="comp_name")
	String compName;
	
	String category;
	
	@Column(name="desig_name")
	String desigName;
	
	@Column(name="joining_date")
	String joiningDate;
	
	@Column(name="contractor_name")
	String contractorName;
	
	@Column(name="emp_code")
	String empCode;
	
	@Column(name="emp_name")
	String empName;
	
	int status;

}
