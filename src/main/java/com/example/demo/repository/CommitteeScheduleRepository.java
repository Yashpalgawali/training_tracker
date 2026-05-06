package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.CommitteeSchedule;

@Repository("committeschedulerepo")
public interface CommitteeScheduleRepository extends JpaRepository<CommitteeSchedule, Long> {

	@Query("SELECT c FROM CommitteeSchedule c WHERE c.committeeScheduleDate LIKE CONCAT('%', :year)")
	public List<CommitteeSchedule> findCommitteeScheduleByYear(@Param("year") String year);
	
	
}
