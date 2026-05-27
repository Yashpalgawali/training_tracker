package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TestScheduleHistory;

@Repository("testschedulehistrepo")
public interface TestScheduleHistRepo extends JpaRepository<TestScheduleHistory, Long> {

}
