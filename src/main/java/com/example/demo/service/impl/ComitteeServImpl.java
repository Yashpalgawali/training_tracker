package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Activity;
import com.example.demo.entity.Committee;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.ActivityRepository;
import com.example.demo.repository.CommitteeRepository;
import com.example.demo.service.ICommitteeService;

import lombok.RequiredArgsConstructor;

@Service("committeeserv")
@RequiredArgsConstructor
public class ComitteeServImpl implements ICommitteeService {

	private final CommitteeRepository committeerepo;
	
	private final ActivityRepository activityrepo;	
 
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	 // Define a custom format pattern
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
 
	@Override
	public void saveCommittee(Committee committee)  {
		String trimmedString = this.trimString(committee.getCommitteeName());
		committee.setCommitteeName(trimmedString);
		
		Committee savedCommittee = committeerepo.save(committee);

		if(savedCommittee != null) {
			Activity activity = Activity.builder().activity("Committee "+trimmedString+" is saved successfully").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
		}
		else {
			Activity activity = Activity.builder().activity("Committee "+trimmedString+" is NOT saved ").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			throw new GlobalException("Committee "+trimmedString+" is not saved");
		}
	}

	@Override
	public List<Committee> getAllCommittees() {
		List<Committee> compList = committeerepo.findAll();
		
		if(compList.size() < 0) {
			throw new ResourceNotFoundException("No Committees found");
		}
		return compList;
		
	}

	@Override
	public Committee getCommitteeById(Long id) {
		Committee companyObject = committeerepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Committee found for given id "+id));
		return companyObject;
	}

	@Override
	@Transactional
	public void updateCommittee(Committee committee) {
		String trimmedString = this.trimString(committee.getCommitteeName());
		committee.setCommitteeName(trimmedString);
		var result = committeerepo.updateCommittee(committee.getCommitteeId(), committee.getCommitteeName());
		if(result>0) {
			Activity activity = Activity.builder().activity("Committee "+committee.getCommitteeName()+" is updated successfully").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
		}
		else {
			Activity activity = Activity.builder().activity("Committee "+committee.getCommitteeName()+" is not Updated ").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			throw new ResourceNotModifiedException("Committee "+committee.getCommitteeName()+" is not updated");
		}
	}

	private String trimString(String string) {
		return string.trim();
	}
}
