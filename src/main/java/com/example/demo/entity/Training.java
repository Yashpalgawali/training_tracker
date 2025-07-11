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
@Table(name = "tbl_training")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Training {

	@Id
	@SequenceGenerator(name="training_seq" , allocationSize = 1,initialValue = 1)
	@GeneratedValue(generator = "training_seq",strategy = GenerationType.IDENTITY)
	Long training_id;

	String training_name;

//	@ManyToMany(mappedBy = "training")
//	@JsonBackReference
//	@ToString.Exclude
//	List<Employee> employee = new ArrayList<>();
	
	/*
	 * @OneToMany(mappedBy = "training") List<EmployeeTraining> employeeTrainings =
	 * new ArrayList<>();
	 */
	
}
 