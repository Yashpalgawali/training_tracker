package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Company;

@Repository("comprepo")
public interface CompanyRepository extends JpaRepository<Company, Long> {

	
	@Query("UPDATE Company c SET c.compName=:compname WHERE c.companyId=:compid")	
	@Modifying
	@Transactional
	public int updateCompany(Long compid,String compname);
	
	@Query("SELECT c FROM Company c WHERE c.compName=:comp_name")
	Company findByComp_name(String comp_name);

}
