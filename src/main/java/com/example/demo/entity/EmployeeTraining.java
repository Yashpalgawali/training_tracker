package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_employee_training")
public class EmployeeTraining {

	@Id
	@SequenceGenerator(name = "employee_training_seq", allocationSize = 1,initialValue = 1)
	@GeneratedValue(generator = "employee_training_seq", strategy = GenerationType.IDENTITY)	
	private Long emp_train_id;
	
	@ManyToOne
	@JoinColumn(name = "emp_id")
	private Employee employee;
	 
	@ManyToOne
	@JoinColumn(name="training_id")
	private Training training;
	
	private LocalDateTime trainingDate; // optional
	
	private LocalDateTime completionDate; // optional
}
