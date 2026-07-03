package com.example.demo.entity;

import java.io.Serializable;

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
@Table(name= "tbl_holiday")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Holiday implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6974610894475432336L;

	@Id
	@SequenceGenerator(name = "holiday_seq" ,allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "holiday_seq",strategy = GenerationType.IDENTITY)
	Long holidayId;
	
	String holiday;
	
	String holidayDate;
	
	//String year;
}
