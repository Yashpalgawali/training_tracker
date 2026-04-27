package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Frequency;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FrequencyRepository;
import com.example.demo.service.IFrequencyService;

import lombok.RequiredArgsConstructor;

@Service("frequencyserv")
@RequiredArgsConstructor
public class FrequencyServImpl implements IFrequencyService {

	private final FrequencyRepository frequencyrepo;
	
	@Override
	public void saveFrequency(Frequency frequency) {
		Frequency savedFrequency =  frequencyrepo.save(frequency);
		if(savedFrequency == null)
			throw new GlobalException("Frequency "+frequency.getFrequency()+" is not saved successfully");
	}

	@Override
	public List<Frequency> getAllFrequencies() {
		var freqList = frequencyrepo.findAll();
		if(freqList.size() > 0 )
			return freqList;
		throw new ResourceNotFoundException("No Frequencies Found");
	}

	@Override
	public Frequency getFrequencyById(Long id) {
		
		return frequencyrepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("No frequency found for given ID "+id));
	}

}
