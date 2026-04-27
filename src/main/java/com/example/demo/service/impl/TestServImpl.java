package com.example.demo.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Test;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.TestRepository;
import com.example.demo.service.ITestService;

import lombok.RequiredArgsConstructor;

@Service("testserv")
@RequiredArgsConstructor
public class TestServImpl implements ITestService {

	private final TestRepository testrepo;
	
	@Override
	public void saveTest(Test test) {
		var savedTest = testrepo.save(test);

		if(savedTest==null) 
			throw new GlobalException("Test "+test.getTestName()+" is not saved");
	}

	@Override
	public Test getTestById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public int updateTest(Test test) {
		
		testrepo.findById(test.getTestingId()).orElseThrow(()-> new ResourceNotFoundException("No test found "));
		return testrepo.updateTest(test.getTestingId(), test.getTestName());
		
	}

}
