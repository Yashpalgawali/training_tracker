package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.TrainingScheduleDto;
import com.example.demo.entity.Training;
import com.example.demo.entity.TrainingSchedule;
import com.example.demo.entity.TrainingScheduleHistory;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.TrainingScheduleMapper;
import com.example.demo.repository.TrainingScheduleHistRepo;
import com.example.demo.repository.TrainingScheduleRepository;
import com.example.demo.service.ITrainingScheduleService;
import com.example.demo.service.ITrainingService;
import com.example.demo.service.IFrequencyService;

import lombok.RequiredArgsConstructor;

@Service("trainingscheduleserv")
@RequiredArgsConstructor
public class TrainingScheduleServImpl implements ITrainingScheduleService {

	private final TrainingScheduleRepository trainingshedulerepo;

	private final TrainingScheduleHistRepo trainingschedulehistrepo;
	
	private final ITrainingService trainingserv;
	
	@Override
	public void saveTrainingSchedule(TrainingScheduleDto training) {
		var trainingObj = trainingserv.getTrainingById(training.getTrainingId());
		
		TrainingSchedule mappedTrainingSchedule = TrainingScheduleMapper.MapToTrainingSchedule(training,
				new TrainingSchedule());
		
		String status = training.getStatus();

		if(status.equals("PLAN")) {
			mappedTrainingSchedule.setPlan(status);
			mappedTrainingSchedule.setDone("");
		}
		if(status.equals("DONE")) {
			mappedTrainingSchedule.setDone(status);
			mappedTrainingSchedule.setPlan("");
		}
		
		String year = training.getTrainingScheduleDate().substring(8, 10);
		Integer in = training.getMonthIndex().intValue();

		switch (in) {
		// Case statements
		case 1:
			mappedTrainingSchedule.setMonthJan("Jan" + year);
			break;
		case 2:
			mappedTrainingSchedule.setMonthFeb("Feb" + year);
			break;
		case 3:
			mappedTrainingSchedule.setMonthMar("Mar" + year);
			break;
		case 4:
			mappedTrainingSchedule.setMonthApr("Apr" + year);
			break;
		case 5:
			mappedTrainingSchedule.setMonthMay("May" + year);
			break;
		case 6:
			mappedTrainingSchedule.setMonthJun("Jun" + year);
			break;
		case 7:
			mappedTrainingSchedule.setMonthJul("Jul" + year);
			break;
		case 8:
			mappedTrainingSchedule.setMonthAug("Aug" + year);
			break;
		case 9:
			mappedTrainingSchedule.setMonthSep("Sep" + year);
			break;
		case 10:
			mappedTrainingSchedule.setMonthOct("Oct" + year);
			break;
		case 11:
			mappedTrainingSchedule.setMonthNov("Nov" + year);
			break;
		case 12:
			mappedTrainingSchedule.setMonthDec("Dec" + year);
			break;
		// Default case statement
		default:
			mappedTrainingSchedule.setMonthDec("Dec" + year);
		}

		Training comm = trainingserv.getTrainingById(training.getTrainingId());
		mappedTrainingSchedule.setTraining(comm);
		
		TrainingSchedule savedEntity = trainingshedulerepo.save(mappedTrainingSchedule);

		if (savedEntity != null) {
			TrainingScheduleHistory histObj = new TrainingScheduleHistory();
			histObj.setTraining(trainingObj.getTraining_name());
			histObj.setTrainingScheduleDate(training.getTrainingScheduleDate());
			histObj.setApprovedBy(training.getApprovedBy());
			histObj.setCheckedBy(training.getCheckedBy());
			histObj.setDoneBy(training.getDoneBy());
			histObj.setFrequency(training.getFrequency());
			histObj.setStatus(training.getStatus());

			trainingschedulehistrepo.save(histObj);
		}	
		

		if (savedEntity == null) {
			throw new GlobalException("Training meeting is not scheduled");
		}
	}

	@Override
	public void updateTrainingSchedule(TrainingScheduleDto training) {

	}

	@Override
	public TrainingSchedule getTrainingScheduleById(Long id) {

		return trainingshedulerepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No Training schedule was found for given ID " + id));
	}

	@Override
	public List<TrainingSchedule> getTrainingScheduleByYear(String year) {
		List<TrainingSchedule>  scheduleList = trainingshedulerepo.findTrainingScheduleByYear(year);
		if(scheduleList.size() > 0 )
			return scheduleList;
		throw new ResourceNotFoundException("No Training schedule was found for given year " + year);
	}

	@Override
	public List<TrainingSchedule> getAllTrainingSchedules() {
		// TODO Auto-generated method stub
		return null;
	}

}
