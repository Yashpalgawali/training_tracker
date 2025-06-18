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
@Table(name = "tbl_department")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Department {

	@Id
	@SequenceGenerator(name="dept_seq",initialValue = 1,allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "dept_seq")
	Long dept_id;

	String dept_name;

	@ManyToOne
	@JoinColumn(name = "company_id")
	Company company;
}
