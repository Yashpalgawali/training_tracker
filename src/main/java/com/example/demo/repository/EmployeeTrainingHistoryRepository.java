package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.EmployeeTraining;
import com.example.demo.entity.EmployeeTrainingHistory;


@Repository("emptrainhistrepo")
public interface EmployeeTrainingHistoryRepository extends JpaRepository<EmployeeTrainingHistory, Long> {

	@Query("SELECT eth FROM EmployeeTrainingHistory eth WHERE eth.employeeTraining.employee.emp_id=:empid")
	List<EmployeeTrainingHistory> getAllTrainingHistoryByEmployeeId(Long empid);
}
