package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Designation;

public interface IDesignationService {

	public Designation saveDesignation(Designation designation);
	
	public Designation getDesignationById(Long desigid);
	
	public List<Designation> getAllDesignations();
	
	public int updateDesignation(Designation designation);
	
	public Object getDesignationByDesignation(String desig_name);
}
