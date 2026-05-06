package com.example.demo.entity;

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
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tbl_test_schedule")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestSchedule {

	@Id
	@SequenceGenerator(name="test_schedule_seq",allocationSize = 1,initialValue = 1)
	@GeneratedValue(generator = "test_schedule_seq",strategy = GenerationType.SEQUENCE)
	Long testScheduleId;

	@ManyToOne
	@JoinColumn(name = "test_id")
	Training test;

	String frequency;

	String testScheduleDate;
	
	String plan;
	String done;

	String doneBy;
	String checkedBy;
	String approvedBy;

	String monthJan;
	String monthFeb;
	String monthMar;
	String monthApr;
	String monthMay;
	String monthJun;
	String monthJul;
	String monthAug;
	String monthSep;
	String monthOct;
	String monthNov;
	String monthDec;

}
