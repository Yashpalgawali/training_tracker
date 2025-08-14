package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Competency;
import com.example.demo.entity.Employee;

@Repository("competencyrepo")
public interface CompetencyRepository extends JpaRepository<Competency, Long> {

	 
}
