package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.CommitteeScheduleDto;
import com.example.demo.entity.CommitteeSchedule;

public interface ICommitteeScheduleService {
	
	public void saveCommitteeSchedule(CommitteeScheduleDto committee);
	
	public void updateCommitteeSchedule(CommitteeScheduleDto committee);
	
	public CommitteeSchedule getCommitteeById(Long id);
	
	public CommitteeSchedule getCommitteeByYear(String year);
	
	public List<CommitteeSchedule> getAllCommitteeSchedules();
	
}
