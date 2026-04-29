package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Test;

public interface ITestService {

	public void saveTest(Test test);
	
	public Test getTestById(Long id);
	
	public int updateTest(Test test);
	
	public List<Test> getAllTests();
}
