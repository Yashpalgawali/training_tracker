package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Holiday;


@Repository("holidayrepo")
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

	Optional<Holiday> findByHolidayDate(String holidayDate);
	
	@Query("UPDATE Holiday h SET h.holiday=:holiday,h.holidayDate=:holidayDate WHERE h.holidayId=:id")
	@Modifying
	public int updateHoliday(String holiday,String holidayDate,Long id);
}
