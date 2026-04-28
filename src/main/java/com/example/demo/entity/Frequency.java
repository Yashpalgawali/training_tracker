package com.example.demo.entity;

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

@Entity @Table(name="tbl_frequency") @Getter @Setter @AllArgsConstructor @NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Frequency {
	@Id
	@SequenceGenerator(name="test_seq",allocationSize = 1,initialValue = 1)
	@GeneratedValue(generator = "test_seq",strategy = GenerationType.SEQUENCE)
	Long frequencyId;
	
	String frequency;
}
