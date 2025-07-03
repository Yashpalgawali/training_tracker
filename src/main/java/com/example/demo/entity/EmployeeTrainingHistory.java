package com.example.demo.entity;

import java.util.List;

import org.springframework.validation.annotation.Validated;

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
@Table(name = "tbl_employee_training_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeTrainingHistory {
 
	@Id
	@SequenceGenerator(name = "emp_train_hist_seq", allocationSize = 1,initialValue = 1)
	@GeneratedValue(generator = "emp_train_hist_seq" ,strategy = GenerationType.AUTO )
	Long emp_train_hist_id;
	
	@ManyToOne
	@JoinColumn(name = "emp_id")
	Employee employee;
	
	@ManyToOne
	@JoinColumn(name = "training_id")
	Training training;
	
	String training_date;
	
	String completion_date;
	
	@Transient
	List<Long> training_ids;
}
