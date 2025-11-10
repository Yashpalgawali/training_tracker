package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.EmployeeHistory;

public interface IEmployeeHistoryService {

	public EmployeeHistory saveEmployeeHistory(EmployeeHistory history);
	
	public List<EmployeeHistory> getEmployeeHistoryByEmployeeCode(String empcode);
	
	public List<EmployeeHistory> getEmployeeHistoryByEmployeeId(Long empid);
}
