package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Frequency;

@Repository("frequencyrepo")
public interface FrequencyRepository extends JpaRepository<Frequency, Long> {

}
