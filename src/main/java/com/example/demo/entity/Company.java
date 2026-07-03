package com.example.demo.entity;

import java.io.Serializable;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Column;
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
public class Company implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8562871672296329812L;

	@Id
	@SequenceGenerator(name="comp_seq" , allocationSize = 1,initialValue = 1)
	@GeneratedValue(generator = "comp_seq",strategy = GenerationType.IDENTITY)
	@Column(name="company_id")
	Long companyId;
	
//	@NotEmpty(message = "Company Name can't be Empty")
	@NotNull(message = "Company Name can't be Empty")
	@Size(min = 2, max =200, message = "Please Enter company Name having at least 2 characters" )
	@Column(name= "comp_name")
	String compName;

}
