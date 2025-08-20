package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Competency;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Training;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CompetencyRepository;
import com.example.demo.service.ICompetencyService;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.ITrainingService;

@Service("competencyserv")
public class CompetencyServImpl implements ICompetencyService {

	private final CompetencyRepository competencyrepo;

	public CompetencyServImpl(CompetencyRepository competencyrepo) {
		super();
		this.competencyrepo = competencyrepo;
	}

	@Override
	public Competency saveCompetency(Competency competency) {
		 
		var comp = competencyrepo.save(competency);
		if (comp != null) {
			return comp;
		} else {
			throw new GlobalException("Competency is not saved");
		}
	}

	@Override
	public List<Competency> getAllCompetencyList() {

		return competencyrepo.findAll();
	}

	@Override
	public Competency getCompetencyById(Long competencyid) {
	 
		return competencyrepo.findById(competencyid).orElseThrow(()-> new ResourceNotFoundException("No Competency found for given ID "+competencyid));
	}

	@Override
	@Transactional
	public int updateCompetency(Competency competency) {

		return competencyrepo.updateCompetencyById(competency.getCompetency_id(), competency.getScore());
	}

}
