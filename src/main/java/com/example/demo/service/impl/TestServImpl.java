package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Test;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceAlreadyExistsException;
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
		String trimmedTestName = test.getTestName().trim();
		
		Optional<Test> foundTest =	testrepo.findByTestName(trimmedTestName);
		if(foundTest.isPresent()) {
			throw new ResourceAlreadyExistsException("Test", "Test Name", trimmedTestName);
		}
		test.setTestName(trimmedTestName);
		test.setFrequency(test.getFrequency().trim());
		var savedTest = testrepo.save(test);

		if(savedTest==null) 
			throw new GlobalException("Test "+test.getTestName()+" is not saved");
	}

	@Override
	public Test getTestById(Long id) {
		
		return testrepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Test found for given ID "+id));
	}

	@Override
	@Transactional
	public int updateTest(Test test) {
		test.setTestName(test.getTestName().trim());
		test.setFrequency(test.getFrequency().trim());
		
		testrepo.findById(test.getTestingId()).orElseThrow(()-> new ResourceNotFoundException("No test found "));
		return testrepo.updateTest(test.getTestingId(), test.getTestName());
		
	}

	@Override
	public List<Test> getAllTests() {
		var testList = testrepo.findAll();
		if(testList.size() > 0)
			return testList;
		throw new ResourceNotFoundException("No Tests are found");
	}

}
