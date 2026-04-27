package com.example.demo.entity;

import jakarta.persistence.Entity;
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
	Long frequencyId;
	
	String frequency;
}
