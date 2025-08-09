package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.TrainingTimeSlot;

@Repository("traintimerepo")
@Transactional
public interface TrainingTimeSlotRepository extends JpaRepository<TrainingTimeSlot, Long> {

	 
}
