package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeHistory;

@Repository("emphistrepo")
public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {

	List<EmployeeHistory> findByEmpCode(String empCode);
	List<EmployeeHistory> findByEmployee(Employee employee);
}
