package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.CommitteeSchedule;

@Repository("committeschedulerepo")
public interface CommitteeScheduleRepository extends JpaRepository<CommitteeSchedule, Long> {

	@Query("SELECT c FROM CommitteeSchedule c WHERE c.committeeScheduleDate LIKE '%-year%'")
	public CommitteeSchedule findCommitteeScheduleByYear(String year);
}
