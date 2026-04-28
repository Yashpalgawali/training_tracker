package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Frequency;

@Repository("frequencyrepo")
public interface FrequencyRepository extends JpaRepository<Frequency, Long> {

	@Query("UPDATE Frequency f SET f.frequency=:frequency WHERE f.frequencyId=:id")
	@Modifying
	public int updateFrequency(Long id, String frequency);
	
}
