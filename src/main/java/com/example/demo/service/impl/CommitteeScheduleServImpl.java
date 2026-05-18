package com.example.demo.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CommitteeScheduleDto;
import com.example.demo.entity.Committee;
import com.example.demo.entity.CommitteeSchedule;
import com.example.demo.entity.CommitteeScheduleHistory;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.mapper.CommitteeScheduleMapper;
import com.example.demo.repository.CommitteeScheduleHistRepo;
import com.example.demo.repository.CommitteeScheduleRepository;
import com.example.demo.service.ICommitteeScheduleService;
import com.example.demo.service.ICommitteeService;
import com.example.demo.service.IFrequencyService;

import lombok.RequiredArgsConstructor;

@Service("committeescheduleserv")
@RequiredArgsConstructor
public class CommitteeScheduleServImpl implements ICommitteeScheduleService {

	private final CommitteeScheduleRepository committeeshedulerepo;

	private final CommitteeScheduleHistRepo committeeschedulehistrepo;
	
	private final ICommitteeService committeeserv;

	private final IFrequencyService frequencyserv;
	
	@Override
	public void saveCommitteeSchedule(CommitteeScheduleDto committee) {
		var committeeObj = committeeserv.getCommitteeById(committee.getCommitteeId());
		var frequencyObj = frequencyserv.getFrequencyById(committee.getFrequencyId());
		
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
		mappedCommitteeSchedule.setFrequency(frequencyObj);
		
		CommitteeSchedule savedEntity = committeeshedulerepo.save(mappedCommitteeSchedule);

		if (savedEntity != null) {
			CommitteeScheduleHistory histObj = new CommitteeScheduleHistory();
			histObj.setCommittee(committeeObj.getCommitteeName());
			histObj.setCommitteeScheduleDate(committee.getCommitteeScheduleDate());
			histObj.setApprovedBy(committee.getApprovedBy());
			histObj.setCheckedBy(committee.getCheckedBy());
			histObj.setDoneBy(committee.getDoneBy());
			histObj.setFrequency(frequencyObj.getFrequency());
			histObj.setStatus(committee.getStatus());

			committeeschedulehistrepo.save(histObj);
		}	
		

		if (savedEntity == null) {
			throw new GlobalException("Committee meeting is not scheduled");
		}
	}

	@Override
	@Transactional
	public void updateCommitteeSchedule(CommitteeScheduleDto committee) {

		
		var committeeObj = committeeserv.getCommitteeById(committee.getCommitteeId());
		var frequencyObj = frequencyserv.getFrequencyById(committee.getFrequencyId());
		
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

//		Committee comm = committeeserv.getCommitteeById(committee.getCommitteeId());
//		mappedCommitteeSchedule.setCommittee(comm);
		mappedCommitteeSchedule.setFrequency(frequencyObj);

		System.err.println("The committee object for updation Mapped Object "+mappedCommitteeSchedule.toString());
		int savedEntity = committeeshedulerepo.updateCommitteeScheduleById( mappedCommitteeSchedule.getCommitteeScheduleId(), mappedCommitteeSchedule.getCommitteeScheduleDate(), mappedCommitteeSchedule.getPlan(), mappedCommitteeSchedule.getDone());

		System.err.println("updated result "+savedEntity);
		
		if (savedEntity > 0) {
			CommitteeScheduleHistory histObj = new CommitteeScheduleHistory();
			histObj.setCommittee(committeeObj.getCommitteeName());
			histObj.setCommitteeScheduleDate(committee.getCommitteeScheduleDate());
			histObj.setApprovedBy(committee.getApprovedBy());
			histObj.setCheckedBy(committee.getCheckedBy());
			histObj.setDoneBy(committee.getDoneBy());
			histObj.setFrequency(frequencyObj.getFrequency());
			histObj.setStatus(committee.getStatus());

			committeeschedulehistrepo.save(histObj);
		}
		else {
			throw new GlobalException("Committee meeting schedule is not updated");
		}
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

	@Override
	public void updateCommitteeScheduleSignatureByYear(Map<String, String> body, String year) {
		
		getCommitteeScheduleByYear(year);
		

		String doneBy = body.get("doneBy");
		String checkedBy = body.get("checkedBy");
		String approvedBy = body.get("approvedBy");
		
		CommitteeSchedule commiteeSchedule = new CommitteeSchedule();
		
		commiteeSchedule.setApprovedBy(approvedBy);
		commiteeSchedule.setCheckedBy(checkedBy);
		commiteeSchedule.setDoneBy(doneBy);
		
	}

	@Override
	public void deleteCommitteeScheduleById(Long committeeId) {

		Optional<CommitteeSchedule> found = committeeshedulerepo.findById(committeeId);  
		if(!found.isPresent()){
			throw new ResourceNotFoundException("No committee schedule is found!!");
		}		 

		CommitteeSchedule committeeSchedule = found.get();
		var frequencyObj = frequencyserv.getFrequencyById(committeeSchedule.getFrequency().getFrequencyId());
		
		CommitteeScheduleHistory histObj = new CommitteeScheduleHistory();
		histObj.setCommittee(committeeSchedule.getCommittee().getCommitteeName());
		histObj.setCommitteeScheduleDate(committeeSchedule.getCommitteeScheduleDate());
		histObj.setApprovedBy(committeeSchedule.getApprovedBy());
		histObj.setCheckedBy(committeeSchedule.getCheckedBy());
		histObj.setDoneBy(committeeSchedule.getDoneBy());
		histObj.setFrequency(frequencyObj.getFrequency());
		if(committeeSchedule.getPlan().equals("plan")) {
			histObj.setStatus("plan");
		}
		
		if(committeeSchedule.getDone().equals("done")) {
			histObj.setStatus("done");
		}
		 
		committeeschedulehistrepo.save(histObj);
		
		committeeshedulerepo.deleteById(committeeId);
		CommitteeSchedule committeeById = getCommitteeById(committeeId);
		 
		if(committeeById == null) {
				throw new ResourceNotModifiedException("Committee Schedule is not deleted");
		}
	}
	 

}
