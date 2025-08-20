package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Competency;

@Repository("competencyrepo")
public interface CompetencyRepository extends JpaRepository<Competency, Long> {


	@Query("UPDATE Competency c SET c.score=:score WHERE c.competency_id=:id")
	@Modifying
	public int updateCompetencyById(Long id, Long score);
}
