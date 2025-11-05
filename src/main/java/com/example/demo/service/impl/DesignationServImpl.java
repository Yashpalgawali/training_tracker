package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Activity;
import com.example.demo.entity.Designation;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.ActivityRepository;
import com.example.demo.repository.DesignationRepository;
import com.example.demo.service.IDesignationService;

@Service("desigserv")
public class DesignationServImpl implements IDesignationService {

	private final DesignationRepository desigrepo;

	private final ActivityRepository activityrepo;

	public DesignationServImpl(DesignationRepository desigrepo, ActivityRepository activityrepo) {
		super();
		this.desigrepo = desigrepo;
		this.activityrepo = activityrepo;
	}

	// Define a custom format pattern
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public Designation saveDesignation(Designation designation) {
		Designation desig = desigrepo.save(designation);
		if (desig != null) {
			Activity activity = Activity.builder().activity("Designation "+desig.getDesigName()+" is saved successfully").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			return desig;
		} else {
			Activity activity = Activity.builder().activity("Designation "+designation.getDesigName()+" is not saved ").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			throw new GlobalException("Designation " + designation.getDesigName() + " is not saved ");
		}
	}

	@Override
	public Designation getDesignationById(Long desigid) {

		return desigrepo.findById(desigid)
				.orElseThrow(() -> new ResourceNotFoundException("No Designation found for given ID " + desigid));
	}

	@Override
	public List<Designation> getAllDesignations() {
		List<Designation> dlist = desigrepo.findAll();
		if (dlist.size() > 0) {
			return dlist;
		} else {
			throw new ResourceNotFoundException("No Designations found");
		}
	}

	@Override
	public int updateDesignation(Designation designation) {
		int res = desigrepo.updateDesignation(designation.getDesigId(), designation.getDesigName());
		if (res > 0) {
			Activity activity = Activity.builder().activity("Designation "+designation.getDesigName()+" is updated successfully").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			return res;
		} else {
			Activity activity = Activity.builder().activity("Designation "+designation.getDesigName()+" is updated successfully").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			throw new ResourceNotModifiedException("Designation " + designation.getDesigName() + " is not updated");
		}
	}

	@Override
	public Object getDesignationByDesignation(String desig_name) {
		return desigrepo.findByDesigName(desig_name);
	}

}
