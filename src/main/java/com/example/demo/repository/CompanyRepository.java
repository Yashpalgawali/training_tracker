package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Company;

@Repository("comprepo")
@Transactional
public interface CompanyRepository extends JpaRepository<Company, Long> {

	
	@Query("UPDATE Company c SET c.comp_name=:compname WHERE c.company_id=:compid")	
	@Modifying
	public int updateCompany(Long compid,String compname);
}
