package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.dto.CommitteeScheduleDto;
import com.example.demo.entity.CommitteeSchedule;

public interface ICommitteeScheduleService {
	
	public void saveCommitteeSchedule(CommitteeScheduleDto committee);
	
	public void updateCommitteeSchedule(CommitteeScheduleDto committee);
	
	public CommitteeSchedule getCommitteeById(Long id);
	
	public List<CommitteeSchedule> getCommitteeScheduleByYear(String year);
	
	public List<CommitteeSchedule> getAllCommitteeSchedules();

	public void updateCommitteeScheduleSignatureByYear(Map<String , String> body,String year);

	public void deleteCommitteeScheduleById(Long committeeId);

	public List<String> sendUpcomingMeetingReminders();
}
