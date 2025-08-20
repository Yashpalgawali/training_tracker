package com.example.demo.entity;

import org.springframework.validation.annotation.Validated;

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
	
	String training_date;
	
	@ManyToOne
	@JoinColumn(name= "training_id")
	Training training;
	
	@ManyToOne
	@JoinColumn(name= "training_time_slot_id")
	TrainingTimeSlot trainingTimeSlot;
	
	@ManyToOne
	@JoinColumn(name= "emp_id")
	Employee employee;

}
