package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.Holiday;
import com.example.demo.service.IHolidayService;

import lombok.RequiredArgsConstructor;

@RequestMapping("holiday")
@RestController
@RequiredArgsConstructor
public class HolidayController {

	private final IHolidayService holidayserv;
	
	@PostMapping("/")
	public ResponseEntity<ResponseDto> saveHoliday(@RequestBody Holiday holiday) {
		
		holidayserv.saveHoliday(holiday);		
		return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED.toString(), "Holiday "+holiday.getHoliday()+" is saved successfully"));
	}
	
	
	@GetMapping("/")
	public ResponseEntity<List<Holiday> > getAllHolidays() {
		
		var list = holidayserv.getAllHolidays();		
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@GetMapping("/date/{date}")
	public ResponseEntity<Holiday> getHolidayByDate(@PathVariable String date) {
		
		Holiday result = holidayserv.getHoliday(date);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
}
