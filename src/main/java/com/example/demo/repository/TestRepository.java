package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Test;


@Repository("testrepo")
public interface TestRepository extends JpaRepository<Test, Long> {

	@Query("UPDATE Test t SET t.testName=:name WHERE t.testingId=:id ")
	@Modifying
	public int updateTest(Long id, String name);
	
	public Optional<Test> findByTestName(String testName);
}
