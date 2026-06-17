package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.demo.dto.TrainingTrackerContactsInfoDto;

@SpringBootApplication
@EnableConfigurationProperties(value = TrainingTrackerContactsInfoDto.class)
public class EmployeePerformanceTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeePerformanceTrackerApplication.class, args);	 
	}
}
