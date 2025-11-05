package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.EmployeeHistory;

@Repository("emphistrepo")
public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {

}
