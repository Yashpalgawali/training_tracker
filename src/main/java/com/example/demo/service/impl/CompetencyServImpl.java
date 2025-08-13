package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Competency;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Training;
import com.example.demo.exception.GlobalException;
import com.example.demo.repository.CompetencyRepository;
import com.example.demo.service.ICompetencyService;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.ITrainingService;

@Service("competencyserv")
public class CompetencyServImpl implements ICompetencyService {

	private final CompetencyRepository competencyrepo;

	private final IEmployeeService empserv;

	private final ITrainingService trainserv;

	public CompetencyServImpl(CompetencyRepository competencyrepo, IEmployeeService empserv,
			ITrainingService trainserv) {
		super();
		this.competencyrepo = competencyrepo;
		this.empserv = empserv;
		this.trainserv = trainserv;
	}

	@Override
	public Competency saveCompetency(Competency competency) {
		Employee emp = empserv.getEmployeeByEmployeeId(competency.getEmployee().getEmp_id());
		
		competency.setEmployee(emp);

		Training training = trainserv.getTrainingById(competency.getTraining().getTraining_id());
		competency.setTraining(training);
		
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
	public List<Competency> getAllCompetenciesbyEmpId(Long empid) {
		Employee emp = empserv.getEmployeeByEmployeeId(empid);
		return competencyrepo.findByEmployee(emp);
			
	}

}
