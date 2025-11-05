package com.example.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
//@EnableCaching
public class EmployeePerformanceTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeePerformanceTrackerApplication.class, args);
		
		 // Define a custom format pattern
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	 
		System.err.println(dateFormatter.format(LocalDateTime.now()));
	}

//	@RequestMapping(value = {"/{path:[^\\.]*}", "/**/{path:[^\\.]*}"})
//    public String forward() {
//        return "forward:/index.html";
//    }
}
