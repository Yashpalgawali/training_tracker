package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Activity;
import com.example.demo.entity.Company;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.ActivityRepository;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.service.ICompanyService;

import lombok.RequiredArgsConstructor;

@Service("compserv") @RequiredArgsConstructor
public class CompanyServImpl implements ICompanyService {

	private final CompanyRepository comprepo;
	
	private final ActivityRepository activityrepo;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	 // Define a custom format pattern
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
 
	@Override
	@CacheEvict(value = "companies", allEntries = true)
	public Company saveCompany(Company company)  {
		Company savedCompany = comprepo.save(company);
		logger.info("Saved company is {} ",savedCompany);
		if(savedCompany != null) {
			Activity activity = Activity.builder().activity("Company "+savedCompany.getCompName()+" is saved successfully").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			return savedCompany;
		}
		else {
			Activity activity = Activity.builder().activity("Company "+company.getCompName()+" is NOT saved ").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			throw new GlobalException("Company "+company.getCompName()+" is not saved");
		}
	}

	@Override
	public List<Company> getAllCompanies() {
		List<Company> compList = comprepo.findAll();
		if(compList.isEmpty()) {
			throw new ResourceNotFoundException("No Companies found");
		}
		return compList;
		
	}

	@Override
	@Cacheable(value = "companies", key = "#id")
	public Company getCompanyById(Long id) {
		System.err.println("Fetched From DB....");
	
		
		return comprepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Company found for given id "+id));
	}

	@Override
	@Transactional
	@CachePut(value = "companies", key = "#company.companyId")
	public Company updateCompany(Company company) {
	    
	    Company foundByName = comprepo.findByCompName(company.getCompName());
	    if (foundByName != null && !foundByName.getCompanyId().equals(company.getCompanyId())) {
	        throw new ResourceAlreadyExistsException("Company", "Name", company.getCompName());
	    }
	    
//	    var result = comprepo.updateCompany(company.getCompanyId(), company.getCompName());
	    var updatedCompany = comprepo.save(company);
	    if(updatedCompany!=null) {
//	    if (result > 0) {
	        Activity activity = Activity.builder()
	                .activity("Company " + company.getCompName() + " is updated successfully")
	                .activityDate(dateFormatter.format(LocalDateTime.now()))
	                .activityTime(timeFormatter.format(LocalDateTime.now()))
	                .build();
	        activityrepo.save(activity);
	        
	        // Return the Company object so @CachePut puts Company in Redis, NOT int
	        return updatedCompany;
	    } else {
	        Activity activity = Activity.builder()
	                .activity("Company " + company.getCompName() + " is not Updated")
	                .activityDate(dateFormatter.format(LocalDateTime.now()))
	                .activityTime(timeFormatter.format(LocalDateTime.now()))
	                .build();
	        activityrepo.save(activity);
	        throw new ResourceNotModifiedException("Company " + company.getCompName() + " is not updated");
	    }
	}

	
//	@Override
//	@Transactional
//	@CachePut(value = "companies", key = "#company.companyId")
//	public Company updateCompany(Company company) {
//		
//		Company foundByName = comprepo.findByCompName(company.getCompName());
//		if(foundByName!=null) {
//			throw new ResourceAlreadyExistsException("Company", "Name", company.getCompName());
//		}
////		var result = comprepo.updateCompany(company.getCompanyId(), company.getCompName());
//		var updatedCompany = comprepo.save(company);
////		if( result > 0 ) {
//		if(updatedCompany!=null) {
//			Activity activity = Activity.builder().activity("Company "+company.getCompName()+" is updated successfully").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
//			activityrepo.save(activity);
//			return updatedCompany;
//		}
//		else {
//			Activity activity = Activity.builder().activity("Company "+company.getCompName()+" is not Updated ").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
//			activityrepo.save(activity);
//			throw new ResourceNotModifiedException("Company "+company.getCompName()+" is not updated");
//		}
//	}

	@Override
	public Company getCompanyByName(String comp_name) {
		return comprepo.findByCompName(comp_name);		
	}

}
