package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.CommitteeScheduleDto;
import com.example.demo.entity.CommitteeSchedule;
import com.example.demo.exception.GlobalException;
import com.example.demo.mapper.CommitteeScheduleMapper;
import com.example.demo.repository.CommitteeScheduleRepository;
import com.example.demo.service.ICommitteeScheduleService;

import lombok.RequiredArgsConstructor;

@Service("committeescheduleserv")
@RequiredArgsConstructor
public class CommitteeScheduleServImpl implements ICommitteeScheduleService {

	private final CommitteeScheduleRepository committeeshedulerepo;

	@Override
	public void saveCommitteeSchedule(CommitteeScheduleDto committee) {
		CommitteeSchedule mappedCommitteeSchedule = CommitteeScheduleMapper.MapToCommitteeSchedule(committee, new CommitteeSchedule());	

		CommitteeSchedule savedEntity = committeeshedulerepo.save(mappedCommitteeSchedule);

		
		if(savedEntity==null) {
			throw new GlobalException("Committee meeting is not scheduled");
		}
	}

	@Override
	public void updateCommitteeSchedule(CommitteeScheduleDto committee) {
		// TODO Auto-generated method stub

	}

	@Override
	public CommitteeSchedule getCommitteeById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommitteeSchedule getCommitteeByYear(String year) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CommitteeSchedule> getAllCommitteeSchedules() {
		// TODO Auto-generated method stub
		return null;
	}

}
