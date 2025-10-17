package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Company;
import com.example.demo.entity.Designation;
import java.util.List;


@Repository("desigrepo")
public interface DesignationRepository extends JpaRepository<Designation, Long> {

	@Query("UPDATE Designation d SET d.desigName=:dname WHERE d.desigId=:did")
	@Transactional
	@Modifying
	public int updateDesignation(Long did, String dname);
	
//	@Query("SELECT d FROM Designation d WHERE d.desigName=:desig_name")
	Designation findByDesigName(@Param("desig_name") String desig_name);
}
 