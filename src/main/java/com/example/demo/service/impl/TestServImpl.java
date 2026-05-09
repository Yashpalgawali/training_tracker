package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.TestingDto;
import com.example.demo.entity.Test;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.TestMapper;
import com.example.demo.repository.TestRepository;
import com.example.demo.service.ITestService;

import lombok.RequiredArgsConstructor;

@Service("testserv")
@RequiredArgsConstructor
public class TestServImpl implements ITestService {

	private final TestRepository testrepo;
	
	@Override
	public void saveTest(TestingDto testDto) {
		String trimmedTestName = testDto.getTestName().trim();
		
		Optional<Test> foundTest =	testrepo.findByTestName(trimmedTestName);
		if(foundTest.isPresent()) {
			throw new ResourceAlreadyExistsException("Test", "Test Name", trimmedTestName);
		}
		testDto.setTestName(trimmedTestName);
		testDto.setFrequency(testDto.getFrequency().trim());
		
		Test test = TestMapper.mapToTest(testDto, new Test());
		var savedTest = testrepo.save(test);

		if(savedTest==null) 
			throw new GlobalException("Test "+testDto.getTestName()+" is not saved");
	}

	@Override
	public Test getTestById(Long id) {
		
		return testrepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Test found for given ID "+id));
	}

	@Override
	@Transactional
	public int updateTest(TestingDto testDto) {
		testrepo.findById(testDto.getTestingId()).orElseThrow(()-> new ResourceNotFoundException("No Test found for given ID "+testDto.getTestingId()));
		
		testDto.setTestName(testDto.getTestName().trim());
		testDto.setFrequency(testDto.getFrequency().trim());
		 
		Test test = TestMapper.mapToTest(testDto, new Test());		
		if(testDto.getTestingId()!= -1) {
			test.setTestingId(testDto.getTestingId());
		}
		
		return testrepo.updateTest(test.getTestingId(), test.getTestName(), test.getFrequency());
		
	}

	@Override
	public List<TestingDto> getAllTests() {
		var testList = testrepo.findAll();
		List<TestingDto> testDtoList = testList.stream().map(test->{
			
			TestingDto testDto = new TestingDto();
			testDto.setTestingId(test.getTestingId());
			testDto.setTestName(test.getTestName());
			testDto.setFrequency(test.getFrequency());
			
			return testDto;			
			
		}).collect(Collectors.toList());
		
		if(testList.size() > 0)
			return testDtoList;
		throw new ResourceNotFoundException("No Tests are found");
	}

}
