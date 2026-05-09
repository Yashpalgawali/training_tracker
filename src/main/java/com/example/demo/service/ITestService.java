package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.TestingDto;
import com.example.demo.entity.Test;

public interface ITestService {

	public void saveTest(TestingDto test);
	
	public Test getTestById(Long id);
	
	public int updateTest(TestingDto test);
	
	public List<TestingDto> getAllTests();
}
