package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeTrainingHistory;

@Repository("emptrainhistrepo")
public interface EmployeeTrainingHistoryRepository extends JpaRepository<EmployeeTrainingHistory, Long> {

	@Query(value= "SELECT et.* FROM tbl_employee_training_history et "
			+ " JOIN tbl_employee e ON et.emp_id=e.emp_id "
			+ " JOIN tbl_training t ON t.training_id=et.training_id "
			+ " WHERE et.emp_id=:empid",nativeQuery = true)
//	@Query("SELECT et FROM EmployeeTrainingHistory et JOIN et.employee JOIN et.training WHERE et.employee.emp_id=:empid")
	List<EmployeeTrainingHistory> findByEmployeeId(Long empid);
	
	
//	List<EmployeeTrainingHistory> findByEmployee(Employee employee);
}
