package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TrainingSchedule;

@Repository("trainingschedulerepo")
public interface TrainingScheduleRepository extends JpaRepository<TrainingSchedule, Long> {

	@Query("SELECT c FROM TrainingSchedule c WHERE c.trainingScheduleDate LIKE CONCAT('%', :year)")
	public List<TrainingSchedule> findTrainingScheduleByYear(@Param("year") String year);
	
	@Query("SELECT c FROM TrainingSchedule c WHERE c.training.training_id=:training_id")
	public List<TrainingSchedule> findTrainingScheduleByTrainingId(@Param("training_id") Long  training_id);

}
