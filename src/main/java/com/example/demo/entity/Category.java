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
@Table(name = "tbl_emp_category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

	@Id
	@SequenceGenerator(name = "emp_category_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "emp_category_seq")
	Long category_id;
	
	String category;
}
