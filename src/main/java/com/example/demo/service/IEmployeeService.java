package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Employee;

public interface IEmployeeService {

	public Employee saveEmployee(Employee emp);
	
	public Employee getEmployeeByEmployeeId(Long empid);
	
	public Employee getEmployeeByEmployeeCode(String empcode);
	
	public int updateEmployee(Employee emp);
	
	public List<Employee> getAllEmployees();
	
}
