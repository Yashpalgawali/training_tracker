package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TestSchedule;

@Repository("testschedulerepo")
public interface TestingScheduleRepository extends JpaRepository<TestSchedule, Long> {

	@Query("SELECT c FROM TestSchedule c WHERE c.testScheduleDate LIKE CONCAT('%', :year)")
	public List<TestSchedule> findTestingScheduleByYear(@Param("year") String year);

	@Query("UPDATE TestSchedule t SET t.doneBy=:doneBy,t.checkedBy=:checkedBy, t.approvedBy=:approvedBy WHERE t.testScheduleDate LIKE CONCAT('%', :year) ")
	@Modifying
	public int updateTestingScheduleSignatureByYear(String doneBy,String checkedBy,String approvedBy,String year);
}
