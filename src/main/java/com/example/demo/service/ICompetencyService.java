package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Competency;

public interface ICompetencyService {

	public Competency saveCompetency(Competency competency);

	public List<Competency> getAllCompetencyList();

	public  Competency getCompetencyById(Long competencyid);
	
}
