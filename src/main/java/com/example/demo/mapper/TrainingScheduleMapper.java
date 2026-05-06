package com.example.demo.mapper;

import com.example.demo.dto.TrainingScheduleDto;
import com.example.demo.entity.TrainingSchedule;

public class TrainingScheduleMapper {

	public static TrainingSchedule MapToTrainingSchedule(TrainingScheduleDto trainingScheduleDto,
			TrainingSchedule trainingSchedule) {

		trainingSchedule.setTrainingScheduleDate(trainingScheduleDto.getTrainingScheduleDate());
		trainingSchedule.setApprovedBy(trainingScheduleDto.getApprovedBy());
		trainingSchedule.setDoneBy(trainingScheduleDto.getDoneBy());
		trainingSchedule.setCheckedBy(trainingScheduleDto.getCheckedBy());
		trainingSchedule.setDone(trainingScheduleDto.getStatus());
		trainingSchedule.setPlan(trainingScheduleDto.getStatus());
		trainingSchedule.setFrequency(trainingScheduleDto.getFrequency());
		
		return trainingSchedule;
	}

	public static TrainingScheduleDto MapToTrainingScheduleDto(TrainingSchedule trainingSchedule,
									TrainingScheduleDto trainingScheduleDto) {

		trainingScheduleDto.setTrainingScheduleDate(trainingSchedule.getTrainingScheduleDate());
		trainingScheduleDto.setApprovedBy(trainingSchedule.getApprovedBy());
		trainingScheduleDto.setDoneBy(trainingSchedule.getDoneBy());
		trainingScheduleDto.setCheckedBy(trainingSchedule.getCheckedBy());
		trainingScheduleDto.setStatus(trainingSchedule.getDone());
		trainingScheduleDto.setFrequency(trainingSchedule.getFrequency());
		
		return trainingScheduleDto;
	}
}
