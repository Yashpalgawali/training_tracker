package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Frequency;

public interface IFrequencyService {
	
	public void saveFrequency(Frequency frequency);
	
	public List<Frequency> getAllFrequencies();
	
	public Frequency getFrequencyById(Long id);
	
}
