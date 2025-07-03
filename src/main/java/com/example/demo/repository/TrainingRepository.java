package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Training;

@Repository("trainrepo")
public interface TrainingRepository extends JpaRepository<Training, Long> {

	@Query("UPDATE Training t SET t.training_name=:tname WHERE t.training_id=:tid")
	@Modifying
	@Transactional
	public int updateTraining(Long tid,String tname);
	
	
	
}
