package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

	@GetMapping("/hello")
	public String hello() {
		return "Hello World";
	}
	
	@GetMapping("/hello-world")
	public String helloWorld() {
		return "Hello World Second API";
	}
}
