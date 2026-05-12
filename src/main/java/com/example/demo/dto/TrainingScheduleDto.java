package com.example.demo.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data @AllArgsConstructor @NoArgsConstructor @FieldDefaults(level = AccessLevel.PRIVATE)
public class TrainingScheduleDto {
	
	Long trainingScheduleId;
	
	Long trainingId;	
	String frequency;
	
	Long monthIndex;
	
	String trainingScheduleDate;
	
	String trainingScheduleEndDate;
	
	String status;
	
	String doneBy;
	String checkedBy;
	String approvedBy;
	
}
