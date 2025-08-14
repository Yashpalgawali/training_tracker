package com.example.demo.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.EmployeeTrainingDto;
import com.example.demo.entity.Competency;
import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.entity.Training;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.CompetencyRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.EmployeeTrainingHistoryRepository;
import com.example.demo.service.ICompetencyService;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.IEmployeeTrainingHistoryService;
import com.example.demo.service.ITrainingService;

@Service("emptrainhistserv")
public class EmployeeTrainingHistoryServImpl implements IEmployeeTrainingHistoryService {

	private final EmployeeTrainingHistoryRepository emptrainhistrepo;

	private final ITrainingService trainserv;

	private final EmployeeRepository emprepo;

	private final CompetencyRepository competencyrepo;

	public EmployeeTrainingHistoryServImpl(EmployeeTrainingHistoryRepository emptrainhistrepo,
			ITrainingService trainserv, EmployeeRepository emprepo, CompetencyRepository competencyrepo) {
		super();
		this.emptrainhistrepo = emptrainhistrepo;
		this.trainserv = trainserv;
		this.emprepo = emprepo;
		this.competencyrepo = competencyrepo;
	}

	private DateTimeFormatter dday = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private DateTimeFormatter ttime = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public int saveEmployeeTrainingHistory(EmployeeTrainingHistory history) {

		List<Long> training_ids = history.getTraining_ids();
		List<EmployeeTrainingHistory> savedhistory = training_ids.stream().map(id -> {

			Training training = trainserv.getTrainingById(id);

			EmployeeTrainingHistory hist = new EmployeeTrainingHistory();

			Employee emp = emprepo.findById(history.getEmployee().getEmp_id()).get();
			
			Competency competency = competencyrepo.findById(history.getCompetency().getCompetency_id()).get();
			
			hist.setCompetency(competency);
			hist.setEmployee(emp);
			hist.setTraining(training);
			hist.setTraining_date((history.getTraining_date()));
			hist.setCompletion_date(history.getCompletion_date());
			hist.setTrainingTimeSlot(history.getTrainingTimeSlot());
			
			System.err.println("OBJECT TO BE SAVED "+hist.toString());
			
			
			emptrainhistrepo.save(hist);
			
			return hist;

		}).collect(Collectors.toList());

		if (!savedhistory.isEmpty()) {
			return 1;
		} else {
			throw new GlobalException("No Training is assigned to the Employee " + history.getEmployee().getEmp_name());
		}
	}

	@Override
	public List<EmployeeTrainingHistory> getAllEmployeesTrainingHistory() {

		return emptrainhistrepo.findAll();
	}

	@Override
	public List<EmployeeTrainingHistory> getEmployeesTrainingHistoryByEmployeeId(Long empid) {

		List<EmployeeTrainingHistory> empHistList = Optional.ofNullable(emptrainhistrepo.findByEmployeeId(empid))
				.orElse(Collections.emptyList());

		return empHistList;
	}

	@Override
	public int updateCompletionTime(Long id, String completion_date) {
		int result = emptrainhistrepo.updateCompletionTime(id, completion_date);
		if (result > 0) {
			return result;
		} else {
			throw new ResourceNotModifiedException("Completion Time is not Updated");
		}
	}

	@Override
	public Training getTrainingByHistId(Long histid) {
		EmployeeTrainingHistory employeeTrainHistoryById = emptrainhistrepo.getEmployeeTrainHistoryById(histid);
		return trainserv.getTrainingById(employeeTrainHistoryById.getTraining().getTraining_id());
	}

	@Override
	public EmployeeTrainingHistory getEmployeeTrainingHistoryByID(Long emptrainhist_id) {

		return emptrainhistrepo.findById(emptrainhist_id)
				.orElseThrow(() -> new ResourceNotFoundException("No Employee Training history Found "));
	}

	@Override
	public List<EmployeeTrainingDto> getAllTrainingListOfAllEmployees() {

		List<EmployeeTrainingDto> trainList = new ArrayList<>();

		List<Object[]> objList = emptrainhistrepo.getAllTrainingsOfAllEmployees();

		if (objList.size() > 0) {
			objList.forEach(train -> {
				EmployeeTrainingDto dto = new EmployeeTrainingDto();

				dto.setEmp_name(train[0].toString());
				dto.setTraining_name(train[1].toString());
				dto.setTraining_date(train[2].toString());
				if (train[3] == null) {
					dto.setCompletion_date(null);
				} else {
					dto.setCompletion_date(train[3].toString());
				}
				dto.setDesig_name(train[4].toString());
				dto.setDept_name(train[5].toString());
				dto.setComp_name(train[6].toString());
				dto.setScore(String.valueOf(train[7]));
				trainList.add(dto);
			});
		}

		return trainList;
	}

}
