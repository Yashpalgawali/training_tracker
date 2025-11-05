package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Activity;
import com.example.demo.entity.Company;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.ActivityRepository;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.service.ICompanyService;

@Service("compserv")
public class CompanyServImpl implements ICompanyService {

	private final CompanyRepository comprepo;
	
	private final ActivityRepository activityrepo;
	
	public CompanyServImpl(CompanyRepository comprepo, ActivityRepository activityrepo) {
		super();
		this.comprepo = comprepo;
		this.activityrepo = activityrepo;
	}

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	 // Define a custom format pattern
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
 
	@Override
	public Company saveCompany(Company company)  {
		Company savedCompany = comprepo.save(company);
		logger.info("Saved company is {} ",savedCompany);
		if(savedCompany != null) {
			Activity activity = Activity.builder().activity("Company "+savedCompany.getCompName()+" is saved successfully").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			return savedCompany;
		}
		else {
			Activity activity = Activity.builder().activity("Company "+savedCompany.getCompName()+" is NOT saved ").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			throw new GlobalException("Company "+company.getCompName()+" is not saved");
		}
	}

	@Override
//	@Cacheable(value = "companyCache", key = "'allCompanies'")
	public List<Company> getAllCompanies() {
		List<Company> compList = comprepo.findAll();
		System.err.println("Calling from DB....");
		if(compList.isEmpty()) {
			throw new ResourceNotFoundException("No Companies found");
		}
		return compList;
		
	}

	@Override
	@Transactional
	public Company getCompanyById(Long comp_id) {
		Company companyObject = comprepo.findById(comp_id).orElseThrow(()-> new ResourceNotFoundException("No Company found for given id "+comp_id));
	 	
//		Company companyObject1 = comprepo.findById(comp_id).orElseThrow(()-> new ResourceNotFoundException("No Company found for given id "+comp_id));

		return companyObject;
	}

	@Override
	public int updateCompany(Company company) {
		
		var result = comprepo.updateCompany(company.getCompanyId(), company.getCompName());
		if(result>0) {
			Activity activity = Activity.builder().activity("Company "+company.getCompName()+" is updated successfully").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			return result;
		}
		else {
			Activity activity = Activity.builder().activity("Company "+company.getCompName()+" is not Updated ").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			throw new ResourceNotModifiedException("Company "+company.getCompName()+" is not updated");
		}
	}

}
