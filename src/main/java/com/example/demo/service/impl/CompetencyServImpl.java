package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Activity;
import com.example.demo.entity.Competency;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ActivityRepository;
import com.example.demo.repository.CompetencyRepository;
import com.example.demo.service.ICompetencyService;

@Service("competencyserv")
public class CompetencyServImpl implements ICompetencyService {

	private final CompetencyRepository competencyrepo;

	private final ActivityRepository activityrepo;

	public CompetencyServImpl(CompetencyRepository competencyrepo, ActivityRepository activityrepo) {
		super();
		this.competencyrepo = competencyrepo;
		this.activityrepo = activityrepo;
	}

	// Define a custom format pattern
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	 

	@Override
	public Competency saveCompetency(Competency competency) {

		var comp = competencyrepo.save(competency);
		if (comp != null) {
			Activity activity = Activity.builder().activity("Competency with score "+comp.getScore()+" is saved successfully").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			return comp;
		} else {
			Activity activity = Activity.builder().activity("Competency with score "+comp.getScore()+" is not saved successfully").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			throw new GlobalException("Competency is not saved");
		}
	}

	@Override
	public List<Competency> getAllCompetencyList() {

		List<Competency> competencyList = competencyrepo.findAll();
		if (competencyList.isEmpty()) {
			throw new ResourceNotFoundException("No Competencies found");
		}
		return competencyList;
	}

	@Override
	public Competency getCompetencyById(Long competencyid) {

		return competencyrepo.findById(competencyid)
				.orElseThrow(() -> new ResourceNotFoundException("No Competency found for given ID " + competencyid));
	}

	@Override
	@Transactional
	public int updateCompetency(Competency competency) {

		int result = competencyrepo.updateCompetencyById(competency.getCompetency_id(), competency.getScore());
		if(result>0) {
			Activity activity = Activity.builder().activity("Competency with score "+competency.getScore()+" is updated successfully").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
		}else {
			Activity activity = Activity.builder().activity("Competency with score "+competency.getScore()+" is not Updated ").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
		}
		return result;
	}

}
