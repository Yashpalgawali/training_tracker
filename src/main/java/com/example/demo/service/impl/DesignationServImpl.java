package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Designation;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.DesignationRepository;
import com.example.demo.service.IDesignationService;

@Service("desigserv")
public class DesignationServImpl implements IDesignationService {

	private final DesignationRepository desigrepo;

	/**
	 * @param desigrepo
	 */
	public DesignationServImpl(DesignationRepository desigrepo) {
		super();
		this.desigrepo = desigrepo;
	}

	@Override
	public Designation saveDesignation(Designation designation) {
		Designation desig = desigrepo.save(designation);
		if(desig!=null) {
			return desig;
		}
		else {
			throw new GlobalException("Designation "+designation.getDesig_name()+" is not saved ");
		}
	}

	@Override
	public Designation getDesignationById(Long desigid) {
		 
		return desigrepo.findById(desigid).orElseThrow(()-> new ResourceNotFoundException("No Designation found for given ID "+desigid));
	}

	@Override
	public List<Designation> getAllDesignations() {
		List<Designation> dlist = desigrepo.findAll();
		if(dlist.size()>0) {
			return dlist;
		}
		else {
			throw new ResourceNotFoundException("No Designations found");
		}
	}

	@Override
	public int updateDesignation(Designation designation) {
		int res = desigrepo.updateDesignation(designation.getDesig_id(), designation.getDesig_name());
		if(res>0) {
			return res ;
		}
		else {
			throw new ResourceNotModifiedException("Designation "+designation.getDesig_name()+" is not updated");
		}
	}

	@Override
	public Object getDesignationByDesignation(String desig_name) {
		return desigrepo.findByDesig_name(desig_name);		
	}

}
