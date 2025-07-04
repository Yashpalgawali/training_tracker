package com.example.demo.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ErrorResponseDto;
import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.entity.Training;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.EmployeeTrainingHistoryRepository;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.IEmployeeTrainingHistoryService;
import com.example.demo.service.ITrainingService;

@Service("emptrainhistserv")
public class EmployeeTrainingHistoryServImpl implements IEmployeeTrainingHistoryService {

	private final EmployeeTrainingHistoryRepository emptrainhistrepo; 
	 
	private final ITrainingService trainserv;
	
	
	public EmployeeTrainingHistoryServImpl(EmployeeTrainingHistoryRepository emptrainhistrepo,  
			ITrainingService trainserv) {
		super();
		this.emptrainhistrepo = emptrainhistrepo;
		 
		this.trainserv = trainserv;
	}

	private DateTimeFormatter dday = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private DateTimeFormatter ttime = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	@Override
	public int saveEmployeeTrainingHistory(EmployeeTrainingHistory history) {
		
		List<Long> training_ids = history.getTraining_ids();
		 List<EmployeeTrainingHistory> savedhistory = training_ids.stream().map(id-> {

			Training training = trainserv.getTrainingById(id);
			
			EmployeeTrainingHistory hist = new EmployeeTrainingHistory();
			hist.setEmployee(history.getEmployee());
			hist.setTraining(training);
			hist.setTraining_date((history.getTraining_date()));
			
			emptrainhistrepo.save(hist); 
			return hist;

		}).collect(Collectors.toList());

		if(!savedhistory.isEmpty()) {
			return 1;
		}
		else {
			throw new GlobalException("No Training is assigned to the Employee "+history.getEmployee().getEmp_name());
		}
	}

	@Override
	public List<EmployeeTrainingHistory> getAllEmployeesTrainingHistory() {

		return emptrainhistrepo.findAll();
	}

	@Override
	public List<EmployeeTrainingHistory> getEmployeesTrainingHistoryByEmployeeId(Long empid) {
//		 Employee emp = empserv.getEmployeeByEmployeeId(empid);
		 
		 List<EmployeeTrainingHistory> empHistList = Optional.ofNullable(
				    emptrainhistrepo.findByEmployeeId(empid)
				).orElse(Collections.emptyList());
//		 List<EmployeeTrainingHistory> empHistList = emptrainhistrepo.findByEmployeeId(empid);
//		 if(empHistList.size()>0)
//			 return empHistList;
//		 else {
//			 throw new ResourceNotFoundException("No Training(s) are given  ");
//		 }
		 return empHistList;
	}

	@Override
	public int updateCompletionTime(Long id, String completion_date) {
		int result = emptrainhistrepo.updateCompletionTime(id, completion_date);
		if(result > 0) {
			return result;
		}
		else {			
			throw new ResourceNotModifiedException("Completion Time is not updated");
		}
	}

	@Override
	public Training getTrainingByHistId(Long histid) {
		 
		EmployeeTrainingHistory employeeTrainHistoryById = emptrainhistrepo.getEmployeeTrainHistoryById(histid);
		 
		return trainserv.getTrainingById(employeeTrainHistoryById.getTraining().getTraining_id());
		 	
	} 
 
	@Override
	public EmployeeTrainingHistory getEmployeeTrainingHistoryByID(Long emptrainhist_id) {
		
		return emptrainhistrepo.findById(emptrainhist_id).orElseThrow(() -> new ResourceNotFoundException("No Employee Training history Found "));		
	}

}
