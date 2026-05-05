package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.CommitteeScheduleDto;
import com.example.demo.entity.Committee;
import com.example.demo.entity.CommitteeSchedule;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.CommitteeScheduleMapper;
import com.example.demo.repository.CommitteeScheduleRepository;
import com.example.demo.service.ICommitteeScheduleService;
import com.example.demo.service.ICommitteeService;

import lombok.RequiredArgsConstructor;

@Service("committeescheduleserv")
@RequiredArgsConstructor
public class CommitteeScheduleServImpl implements ICommitteeScheduleService {

	private final CommitteeScheduleRepository committeeshedulerepo;

	private final ICommitteeService committeeserv;

	@Override
	public void saveCommitteeSchedule(CommitteeScheduleDto committee) {
		CommitteeSchedule mappedCommitteeSchedule = CommitteeScheduleMapper.MapToCommitteeSchedule(committee,
				new CommitteeSchedule());
		
		String status = committee.getStatus();

		if(status.equals("PLAN")) {
			mappedCommitteeSchedule.setPlan(status);
			mappedCommitteeSchedule.setDone("");
		}
		if(status.equals("DONE")) {
			mappedCommitteeSchedule.setDone(status);
			mappedCommitteeSchedule.setPlan("");
		}
		
		String year = committee.getCommitteeScheduleDate().substring(8, 10);
		Integer in = committee.getMonthIndex().intValue();

		switch (in) {
		// Case statements
		case 1:
			mappedCommitteeSchedule.setMonthJan("Jan" + year);
			break;
		case 2:
			mappedCommitteeSchedule.setMonthFeb("Feb" + year);
			break;
		case 3:
			mappedCommitteeSchedule.setMonthMar("Mar" + year);
			break;
		case 4:
			mappedCommitteeSchedule.setMonthApr("Apr" + year);
			break;
		case 5:
			mappedCommitteeSchedule.setMonthMay("May" + year);
			break;
		case 6:
			mappedCommitteeSchedule.setMonthJun("Jun" + year);
			break;
		case 7:
			mappedCommitteeSchedule.setMonthJul("Jul" + year);
			break;
		case 8:
			mappedCommitteeSchedule.setMonthAug("Aug" + year);
			break;
		case 9:
			mappedCommitteeSchedule.setMonthSep("Sep" + year);
			break;
		case 10:
			mappedCommitteeSchedule.setMonthOct("Oct" + year);
			break;
		case 11:
			mappedCommitteeSchedule.setMonthNov("Nov" + year);
			break;
		case 12:
			mappedCommitteeSchedule.setMonthDec("Dec" + year);
			break;
		// Default case statement
		default:
			mappedCommitteeSchedule.setMonthDec("Dec" + year);
		}

		Committee comm = committeeserv.getCommitteeById(committee.getCommitteeId());
		mappedCommitteeSchedule.setCommittee(comm);
		CommitteeSchedule savedEntity = committeeshedulerepo.save(mappedCommitteeSchedule);

		if (savedEntity == null) {
			throw new GlobalException("Committee meeting is not scheduled");
		}
	}

	@Override
	public void updateCommitteeSchedule(CommitteeScheduleDto committee) {

	}

	@Override
	public CommitteeSchedule getCommitteeById(Long id) {

		return committeeshedulerepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No Committee meetings was found for given ID " + id));
	}

	@Override
	public List<CommitteeSchedule> getCommitteeScheduleByYear(String year) {
		List<CommitteeSchedule>  scheduleList = committeeshedulerepo.findCommitteeScheduleByYear(year);
		if(scheduleList.size() > 0 )
			return scheduleList;
		throw new ResourceNotFoundException("No Committee meetings was found for given year " + year);
	}

	@Override
	public List<CommitteeSchedule> getAllCommitteeSchedules() {
		// TODO Auto-generated method stub
		return null;
	}

}
