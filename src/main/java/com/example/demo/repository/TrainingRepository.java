package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Training;

@Repository("trainrepo")
public interface TrainingRepository extends JpaRepository<Training, Long> {

	@Query("UPDATE Training t SET t.training_name=:tname,t.frequency=:frequency WHERE t.training_id=:tid")
	@Modifying
	public int updateTraining(Long tid,String tname,String frequency);
	
	@Query("SELECT t FROM Training t WHERE t.training_name=:training_name")
	public Optional<Training> findByTraining_name(String training_name);
	
}
