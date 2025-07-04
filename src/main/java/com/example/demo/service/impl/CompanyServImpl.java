package com.example.demo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Company;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.service.ICompanyService;

@Service("compserv")
public class CompanyServImpl implements ICompanyService {

	private final CompanyRepository comprepo;

	public CompanyServImpl(CompanyRepository comprepo) {
		super();
		this.comprepo = comprepo;
	}

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Company saveCompany(Company company)  {
		Company savedCompany = comprepo.save(company);
		logger.info("Saved company is {} ",savedCompany);
		if(savedCompany != null) {
			return savedCompany;
		}
		else {
			throw new GlobalException("Company "+company.getComp_name()+" is not saved");
		}
	}

	@Override
	public List<Company> getAllCompanies() {
		List<Company> compList = comprepo.findAll();
		return compList;
		
	}

	@Override
	public Company getCompanyById(Long comp_id) {
		return comprepo.findById(comp_id).orElseThrow(()-> new ResourceNotFoundException("No Company found for given id "+comp_id));
	}

	@Override
	public int updateCompany(Company company) {
		
		var result = comprepo.updateCompany(company.getCompany_id(), company.getComp_name());
		if(result>0) {
			return result;
		}
		else {
			throw new ResourceNotModifiedException("Company "+company.getComp_name()+" is not updated");
		}
	}

}
