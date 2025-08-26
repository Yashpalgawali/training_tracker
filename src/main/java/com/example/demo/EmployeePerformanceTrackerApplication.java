package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class EmployeePerformanceTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeePerformanceTrackerApplication.class, args);
	}

	@RequestMapping(value = {"/{path:[^\\.]*}", "/**/{path:[^\\.]*}"})
    public String forward() {
        return "forward:/index.html";
    }
}
