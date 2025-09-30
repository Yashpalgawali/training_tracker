package com.example.demo.entity;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tbl_company")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
@Cacheable
public class Company {

	@Id
	@SequenceGenerator(name="comp_seq" , allocationSize = 1,initialValue = 1)
	@GeneratedValue(generator = "comp_seq",strategy = GenerationType.IDENTITY)
	Long company_id;
	
//	@NotEmpty(message = "Company Name can't be Empty")
	@NotNull(message = "Company Name can't be Empty")
	@Size(min = 2, max =200, message = "Please Enter company Name having at least 2 characters" )
	String comp_name;

}
