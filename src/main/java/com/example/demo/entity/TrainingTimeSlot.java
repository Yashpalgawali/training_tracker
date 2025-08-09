package com.example.demo.entity;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "tbl_training_time_slot")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrainingTimeSlot {
	
	@Id
	@SequenceGenerator(name = "train_time_slot_seq", allocationSize = 1,initialValue = 1)
	@GeneratedValue(generator = "train_time_slot_seq" ,strategy = GenerationType.AUTO )
	Long training_time_slot_id;
	
	String training_time_slot;
}
