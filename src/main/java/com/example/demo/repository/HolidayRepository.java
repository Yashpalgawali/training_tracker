package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Holiday;


@Repository("holidayrepo")
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

	Optional<Holiday> findByHolidayDate(String holidayDate);
}
