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
import lombok.experimental.FieldDefaults;

@Entity @Table(name="tbl_committee") @Getter @Setter @AllArgsConstructor @NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Committee implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1824770429486374222L;

	@Id
	@SequenceGenerator(name="committee_seq",allocationSize = 1,initialValue = 1)
	@GeneratedValue(generator = "committee_seq",strategy = GenerationType.SEQUENCE)
	Long committeeId;
	
	String committeeName; 
}
