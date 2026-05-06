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

@Entity
@Table(name = "tbl_committee_schedule_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommitteeScheduleHistory {

	@Id
	@SequenceGenerator(name="committee_schedule_hist_seq",allocationSize = 1,initialValue = 1)
	@GeneratedValue(generator = "committee_schedule_hist_seq",strategy = GenerationType.SEQUENCE)
	Long committeeScheduleHistId;

	String committee;

	String frequency;

	String committeeScheduleDate;
	
	String status;

	String doneBy;
	String checkedBy;
	String approvedBy;

}
