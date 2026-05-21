package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Training;

public interface IEmployeeService {

	public Employee saveEmployee(Employee empDto);
	
	public Employee getEmployeeByEmployeeId(Long empid);
	
	public Employee getEmployeeByEmployeeCode(String empcode);	
	
	public int updateEmployee(Employee emp);
	
	public List<Employee> getAllEmployees();
	
	public List<EmployeeDTO> getAllEmployeesWithoudTrainingAndCompetency(Long training_id,Long competency_id,String tdate,Long timeslot);

	public int checkEmployeeAttendedTrainingOnDateAndTimeSlot(Long id,Long timeslot,String training_date);
	
	public List<Employee> getAllActiveEmployees();
	
//	public Map<String, Object> getAllEmployeesWithPagination(int page,int size,String searchValue);
	
	public Map<String, Object> getAllEmployeesWithPagination(int page,int size,String search,String orderColumn, String orderDir );
	
	public List<Training> getAllTrainingsByEmployeeId(Long empid);
	
	/**
	 * Processes the uploaded Excel file in a background thread (@Async).
	 * The controller must pass the raw bytes (not an InputStream) because
	 * the MultipartFile stream is closed once the HTTP request completes.
	 *
	 * @param fileBytes raw bytes of the uploaded .xlsx file
	 */
	public void uploadEmployeeList(byte[] fileBytes);
		
}
