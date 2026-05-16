package com.example.demo.mapper;

import com.example.demo.dto.CommitteeScheduleDto;
import com.example.demo.entity.CommitteeSchedule;

public class CommitteeScheduleMapper {

	public static CommitteeSchedule MapToCommitteeSchedule(CommitteeScheduleDto committeeScheduleDto,
			CommitteeSchedule committeeSchedule) {

		committeeSchedule.setCommitteeScheduleId(committeeScheduleDto.getCommitteeScheduleId());
		committeeSchedule.setCommitteeScheduleDate(committeeScheduleDto.getCommitteeScheduleDate());
		committeeSchedule.setApprovedBy(committeeScheduleDto.getApprovedBy());
		committeeSchedule.setDoneBy(committeeScheduleDto.getDoneBy());
		committeeSchedule.setCheckedBy(committeeScheduleDto.getCheckedBy());
		committeeSchedule.setDone(committeeScheduleDto.getStatus());
		committeeSchedule.setPlan(committeeScheduleDto.getStatus());
		
		return committeeSchedule;
	}

	public static CommitteeScheduleDto MapToCommitteeScheduleDto(CommitteeSchedule committeeSchedule,
									CommitteeScheduleDto committeeScheduleDto) {

		committeeScheduleDto.setCommitteeScheduleId(committeeSchedule.getCommitteeScheduleId());
		committeeScheduleDto.setCommitteeScheduleDate(committeeSchedule.getCommitteeScheduleDate());
		committeeScheduleDto.setApprovedBy(committeeSchedule.getApprovedBy());
		committeeScheduleDto.setDoneBy(committeeSchedule.getDoneBy());
		committeeScheduleDto.setCheckedBy(committeeSchedule.getCheckedBy());
		committeeScheduleDto.setStatus(committeeSchedule.getDone());
		
		return committeeScheduleDto;
	}
}
