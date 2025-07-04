package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Designation;

@Repository("desigrepo")
public interface DesignationRepository extends JpaRepository<Designation, Long> {

	@Query("UPDATE Designation d SET d.desig_name=:dname WHERE d.desig_id=:did")
	@Transactional
	@Modifying
	public int updateDesignation(Long did, String dname);
}
