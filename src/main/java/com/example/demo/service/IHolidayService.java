package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Holiday;

public interface IHolidayService {

	public Holiday saveHoliday(Holiday holiday);
	
	public List<Holiday> getAllHolidays();
	
}
