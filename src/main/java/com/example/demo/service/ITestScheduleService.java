package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.dto.TestingScheduleDto;
import com.example.demo.entity.TestSchedule;

public interface ITestScheduleService {
	
	public void saveTestSchedule(TestingScheduleDto training);
	
	public void updateTestSchedule(TestingScheduleDto training);
	
	public TestSchedule getTestScheduleById(Long id);
	
//	public List<TestingScheduleDto> getTestScheduleByYear(String year);
	public List<TestSchedule> getTestScheduleByYear(String year);
	
	public List<TestSchedule> getAllTestSchedules();
	
	public void updateTestScheduleSignatureByYear(Map<String , String> body,String year);
	
	public void deleteTestScheduleById(Long testScheduleId); 
}
