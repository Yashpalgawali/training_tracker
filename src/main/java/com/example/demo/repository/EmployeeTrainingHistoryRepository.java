package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.entity.Training;


@Repository("emptrainhistrepo")
public interface EmployeeTrainingHistoryRepository extends JpaRepository<EmployeeTrainingHistory, Long> {

	@Query("SELECT eth FROM EmployeeTrainingHistory eth WHERE eth.employee.empId=:empid")
	List<EmployeeTrainingHistory> getAllTrainingHistoryByEmployeeId(Long empid);
	
	@Query("SELECT COUNT(eth.employee.empId) FROM  EmployeeTrainingHistory eth WHERE eth.training.training_id=:training_id AND eth.employee.empId=:empid ")
	long countByEmployeeAndTraining(Long empid,Long training_id);

	List<EmployeeTrainingHistory> findByEmployeeAndTraining(Employee emp,Training train );
}
