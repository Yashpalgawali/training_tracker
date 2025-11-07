package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.EmployeeTraining;
import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.repository.EmployeeTrainingHistoryRepository;
import com.example.demo.repository.EmployeeTrainingRepository;
import com.example.demo.service.IEmployeeTrainingHistoryService;
import com.example.demo.service.IEmployeeTrainingService;

@Service("emptrainhistserv")
public class EmployeeTrainingHistoryServImpl implements IEmployeeTrainingHistoryService {

	private final EmployeeTrainingHistoryRepository emptrainhistrepo;
	private final EmployeeTrainingRepository emptrainrepo;

	public EmployeeTrainingHistoryServImpl(EmployeeTrainingHistoryRepository emptrainhistrepo,
			EmployeeTrainingRepository emptrainrepo) {
		super();
		this.emptrainhistrepo = emptrainhistrepo;
		this.emptrainrepo = emptrainrepo;
	}

	@Override
	public EmployeeTrainingHistory saveEmployeeTrainingHistory(EmployeeTrainingHistory emptrainhist) {
		return emptrainhistrepo.save(emptrainhist);

	}

	@Override
	public List<EmployeeTrainingHistory> getAllEmployeeTrainingHistoryByEmployeeId(Long empid) {

		return emptrainhistrepo.getAllTrainingHistoryByEmployeeId(empid);
	}

	@Override
	public long getCountOfTrainingsByTrainId(Long train_id) {
		EmployeeTraining train = emptrainrepo.findById(train_id).get();
		Long empId = train.getEmployee().getEmpId();
		Long training_id = train.getTraining().getTraining_id();
		return emptrainhistrepo.countByEmployeeAndTraining(empId, training_id);
	}
}
