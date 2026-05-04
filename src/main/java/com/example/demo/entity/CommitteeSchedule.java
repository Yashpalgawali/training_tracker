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
@Table(name = "tbl_committee_schedule")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommitteeSchedule {

	@Id
	@SequenceGenerator(name="committee_schedule_seq",allocationSize = 1,initialValue = 1)
	@GeneratedValue(generator = "committee_schedule_seq",strategy = GenerationType.SEQUENCE)
	Long committeeScheduleId;

	@ManyToOne
	@JoinColumn(name = "committee_id")
	Committee committee;

	@ManyToOne
	@JoinColumn(name = "frequency_id")
	Frequency frequency;

	String committeeScheduleDate;
	
	String plan;
	String done;

	String doneBy;
	String checkedBy;
	String approvedBy;

	String month;

}
