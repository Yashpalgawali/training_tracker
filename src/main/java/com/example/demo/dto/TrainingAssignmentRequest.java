package com.example.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class TrainingAssignmentRequest {
	Long emp_train_id;
	Long trainingId;
    String trainingDate;
    String completionDate;
    Long trainingTimeSlotId;
    Long competencyId;
    List<Long> employeeIds;
}
