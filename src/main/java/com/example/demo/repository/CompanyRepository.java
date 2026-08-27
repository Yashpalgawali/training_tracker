package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Company;

@Repository("comprepo")
public interface CompanyRepository extends JpaRepository<Company, Long> {

	@Query("UPDATE Company c SET c.compName=:compname WHERE c.companyId=:compid")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	public int updateCompany(Long compid, String compname);

	Company findByCompName(String compName);

}
