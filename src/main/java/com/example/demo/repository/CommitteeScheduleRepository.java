package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.CommitteeSchedule;

@Repository("committeschedulerepo")
public interface CommitteeScheduleRepository extends JpaRepository<CommitteeSchedule, Long> {

	@Query("SELECT c FROM CommitteeSchedule c WHERE c.committeeScheduleDate LIKE CONCAT('%', :year)")
	public List<CommitteeSchedule> findCommitteeScheduleByYear(@Param("year") String year);
	
	
	@Query("UPDATE CommitteeSchedule t SET t.doneBy=:doneBy,t.checkedBy=:checkedBy, t.approvedBy=:approvedBy WHERE t.committeeScheduleDate LIKE CONCAT('%', :year) ")
	@Modifying
	public int updateTestingScheduleSignatureByYear(String doneBy,String checkedBy,String approvedBy,String year);
	
	
	@Query("UPDATE CommitteeSchedule t SET t.committeeScheduleDate=:committeeScheduleDate,t.done=:done,t.plan=:plan WHERE t.committeeScheduleId=:commiteeScheduleId")
	@Modifying
	public int updateCommitteeScheduleById(Long commiteeScheduleId,String committeeScheduleDate,String plan, String done);


}
