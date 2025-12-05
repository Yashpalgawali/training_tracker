package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Holiday;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.HolidayRepository;
import com.example.demo.service.IHolidayService;

import lombok.RequiredArgsConstructor;

@Service("holidayserv")
@RequiredArgsConstructor
public class HolidayServImpl implements IHolidayService {

	private final HolidayRepository holidayrepo; 
	
	@Override
	public Holiday saveHoliday(Holiday holiday) {
		Holiday savedHoliday = holidayrepo.save(holiday);
		if(savedHoliday!=null) {
			return savedHoliday;
		}
		throw new GlobalException("Holiday "+holiday.getHoliday()+" is not saved");
	}

	@Override
	public List<Holiday> getAllHolidays() {
		List<Holiday> holidayList = holidayrepo.findAll();
		if(holidayList.size() > 0 ) {
			return holidayList;
		}
		throw new ResourceNotFoundException("No Holidays are found");
	}

	@Override
	public Holiday getHoliday(String holidayDate) {
		Optional<Holiday> result = holidayrepo.findByHolidayDate(holidayDate);
		if(result.isPresent()) {
			return result.get();
		}
		return null;
	}

}
