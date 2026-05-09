package com.example.demo.mapper;

import com.example.demo.dto.TestingScheduleDto;
import com.example.demo.entity.TestSchedule;

public class TestingScheduleMapper {

	public static TestSchedule MapToTestingSchedule(TestingScheduleDto testScheduleDto,
			TestSchedule testSchedule) {

		testSchedule.setTestScheduleDate(testScheduleDto.getTestScheduleDate());
		testSchedule.setApprovedBy(testScheduleDto.getApprovedBy());
		testSchedule.setDoneBy(testScheduleDto.getDoneBy());
		testSchedule.setCheckedBy(testScheduleDto.getCheckedBy());
		testSchedule.setDone(testScheduleDto.getStatus());
		testSchedule.setPlan(testScheduleDto.getStatus());
		testSchedule.setFrequency(testScheduleDto.getFrequency());
		
		return testSchedule;
	}

	public static TestingScheduleDto MapToTestingScheduleDto(TestSchedule testSchedule,
									TestingScheduleDto testScheduleDto) {

		testScheduleDto.setTestingScheduleId(testSchedule.getTestScheduleId());
		testScheduleDto.setTestScheduleDate(testSchedule.getTestScheduleDate());
		testScheduleDto.setApprovedBy(testSchedule.getApprovedBy());
		testScheduleDto.setDoneBy(testSchedule.getDoneBy());
		testScheduleDto.setCheckedBy(testSchedule.getCheckedBy());
		testScheduleDto.setStatus(testSchedule.getDone());
		testScheduleDto.setFrequency(testSchedule.getFrequency());
		
		return testScheduleDto;
	}
}
