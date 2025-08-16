package com.example.demo.service;

import java.io.InputStream;
import java.util.List;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Training;

public interface IEmployeeService {

	public Employee saveEmployee(Employee empDto);
	
	public Employee getEmployeeByEmployeeId(Long empid);
	
	public Employee getEmployeeByEmployeeCode(String empcode);
	
	public int updateEmployee(Employee emp);
	
	public List<Employee> getAllEmployees();
	
	public List<Training> getAllTrainingsByEmployeeId(Long empid);
	
	public void uploadEmployeeList(InputStream is);
		
}
