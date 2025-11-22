package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeTraining;
import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.entity.Training;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.EmployeeTrainingHistoryRepository;
import com.example.demo.repository.EmployeeTrainingRepository;
import com.example.demo.repository.TrainingRepository;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.IEmployeeTrainingHistoryService;
import com.example.demo.service.ITrainingService;

@Service("emptrainhistserv")
public class EmployeeTrainingHistoryServImpl implements IEmployeeTrainingHistoryService {

	private final EmployeeTrainingHistoryRepository emptrainhistrepo;
	private final EmployeeTrainingRepository emptrainrepo;
	private final EmployeeRepository emprepo;
	private final TrainingRepository trainrepo;

	public EmployeeTrainingHistoryServImpl(EmployeeTrainingHistoryRepository emptrainhistrepo,
			EmployeeTrainingRepository emptrainrepo, EmployeeRepository emprepo, TrainingRepository trainrepo) {
		super();
		this.emptrainhistrepo = emptrainhistrepo;
		this.emptrainrepo = emptrainrepo;
		this.emprepo = emprepo;
		this.trainrepo = trainrepo;
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

	@Override
	public long getCountOfTrainingsByTrainingIdAndEmployeeId(Long train_id, Long emp_id) {

		return emptrainhistrepo.countByEmployeeAndTraining(emp_id, train_id);
	}

	@Override
	public List<EmployeeTrainingHistory> getEmployeeTrainingHistoryByEmployeeIdAndTrainingId(Long empid, Long tid) {

		Training train = trainrepo.findById(tid).orElse(null);

		Employee employee = emprepo.findById(empid).orElse(null);

		List<EmployeeTrainingHistory> trainHistList = emptrainhistrepo.findByEmployeeAndTraining(employee, train);
		if(trainHistList.size() > 0 )
		{
			return trainHistList;
		}
		throw new ResourceNotFoundException("No Training history found for training "+train.getTraining_name()+" of employee "+employee.getEmpName());
	}
}
