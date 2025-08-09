package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.TrainingTimeSlot;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.TrainingTimeSlotRepository;
import com.example.demo.service.ITrainingTimeSlotService;

@Service("traintimeslotserv")
public class TrainingTimeSlotServImpl implements ITrainingTimeSlotService {

	private final TrainingTimeSlotRepository traintimeslotrepo;
	
	public TrainingTimeSlotServImpl(TrainingTimeSlotRepository traintimeslotrepo) {
		super();
		this.traintimeslotrepo = traintimeslotrepo;
	}

	@Override
	public TrainingTimeSlot saveTrainingTimeSlot(TrainingTimeSlot timeSlot) {
		var savedSlot = traintimeslotrepo.save(timeSlot);
		if(savedSlot!=null)
		{
			return savedSlot;
		}
		else {
			throw new GlobalException("Training Time Slot is not saved");
		}
	}

	@Override
	public List<TrainingTimeSlot> getAllCompanies() {
		
		return traintimeslotrepo.findAll();
	}

	@Override
	public TrainingTimeSlot getTrainingTimeSlotById(Long time_slot_id) {
		return traintimeslotrepo.findById(time_slot_id).orElseThrow(()->new ResourceNotFoundException("Training Time slot not found for given ID "+time_slot_id));
	}

	@Override
	public int updateTrainingTimeSlot(TrainingTimeSlot timeSlot) {
		 var result =traintimeslotrepo.save(timeSlot);
		 if(result!=null) {
			 return 1;
		 }
		 else {
			 throw new GlobalException("Training Time Slot"+timeSlot.getTraining_time_slot()+" is not Updated");
		 }
			 
	}

}
