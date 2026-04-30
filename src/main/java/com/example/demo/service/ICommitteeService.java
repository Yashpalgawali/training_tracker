package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Committee;

public interface ICommitteeService {
	
	public void saveCommittee(Committee committee);
	
	public void updateCommittee(Committee committee);
	
	public Committee getCommitteeById(Long id);
	
	public List<Committee> getAllCommittees();
	
}
