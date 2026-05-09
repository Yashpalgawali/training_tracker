package com.example.demo.mapper;

import com.example.demo.dto.TestingDto;
import com.example.demo.entity.Test;

public class TestMapper {

public static Test mapToTest(TestingDto testDto, Test test) {
		
		test.setTestName(testDto.getTestName());
		test.setFrequency(testDto.getFrequency());
		
		return test;
	}

	public static TestingDto mapToTestDto(Test test, TestingDto testDto) {
		
		testDto.setTestName(test.getTestName());
		testDto.setFrequency(test.getFrequency());
		testDto.setTestingId(test.getTestingId());
		
		return testDto;
	}
}
