package com.example.demo.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.EmployeeTrainingDto;
import com.example.demo.entity.Competency;
import com.example.demo.entity.CompetencyScore;
import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeTraining;
import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.entity.Training;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.CompetencyRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.EmployeeTrainingRepository;
import com.example.demo.service.ICompetencyService;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.IEmployeeTrainingHistoryService;
import com.example.demo.service.IEmployeeTrainingService;
import com.example.demo.service.ITrainingService;

@Service("emptrainserv")
public class EmployeeTrainingServImpl implements IEmployeeTrainingService {

	private final EmployeeTrainingRepository emptrainrepo;

	private final ITrainingService trainserv;
	
	private final IEmployeeTrainingHistoryService emptrainhistserv;

	private final EmployeeRepository emprepo;

	private final CompetencyRepository competencyrepo;

	public EmployeeTrainingServImpl(EmployeeTrainingRepository emptrainrepo, ITrainingService trainserv,
			IEmployeeTrainingHistoryService emptrainhistserv, EmployeeRepository emprepo,
			CompetencyRepository competencyrepo) {
		super();
		this.emptrainrepo = emptrainrepo;
		this.trainserv = trainserv;
		this.emptrainhistserv = emptrainhistserv;
		this.emprepo = emprepo;
		this.competencyrepo = competencyrepo;
	}

	private DateTimeFormatter dday = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private DateTimeFormatter ttime = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public int saveEmployeeTraining(EmployeeTraining training) {

//		List<Long> training_ids = training.getTraining_ids();

//		List<EmployeeTraining> savedhistory = training_ids.stream().map(id -> {
//
//			Training trainingObject = trainserv.getTrainingById(id);
//
//			EmployeeTraining hist = new EmployeeTraining();
//
//			Employee emp = emprepo.findById(training.getEmployee().getEmp_id()).get();
//
//			Competency competency = competencyrepo.findById(training.getCompetency().getCompetency_id()).get();
//
//			hist.setCompetency(competency);
//			hist.setEmployee(emp);
//			hist.setTraining(trainingObject);
//			hist.setTraining_date((training.getTraining_date()));
//			hist.setCompletion_date(training.getCompletion_date());
//			hist.setTrainingTimeSlot(training.getTrainingTimeSlot());
//
//			System.err.println("OBJECT TO BE SAVED " + hist.toString());
//
//			EmployeeTraining savedEmpTraining = emptrainrepo.save(hist);
//			if(savedEmpTraining!=null) {
//				EmployeeTrainingHistory emptrainhist = new EmployeeTrainingHistory();
//				emptrainhist.setEmployeeTraining(savedEmpTraining);
//				emptrainhistserv.saveEmployeeTrainingHistory(emptrainhist);
//				
//			}
//			return hist;
//
//		}).collect(Collectors.toList());
		
		Long training_ids = training.getTraining_ids();
		
		Training trainingObject = trainserv.getTrainingById(training_ids);

		EmployeeTraining train = new EmployeeTraining();

		Employee emp = emprepo.findById(training.getEmployee().getEmp_id()).get();

		Competency competency = competencyrepo.findById(training.getCompetency().getCompetency_id()).get();

		train.setCompetency(competency);
		train.setEmployee(emp);
		train.setTraining(trainingObject);
		train.setTraining_date((training.getTraining_date()));
		train.setCompletion_date(training.getCompletion_date());
		train.setTrainingTimeSlot(training.getTrainingTimeSlot());

		System.err.println("OBJECT TO BE SAVED " + train.toString());

		EmployeeTraining savedEmpTraining = emptrainrepo.save(train);
		if(savedEmpTraining!=null) {
			EmployeeTrainingHistory emptrainhist = new EmployeeTrainingHistory();
			emptrainhist.setEmployeeTraining(savedEmpTraining);
			emptrainhistserv.saveEmployeeTrainingHistory(emptrainhist); 
		 
			return 1;
		} else {
			throw new GlobalException("No Training is assigned to the Employee " + training.getEmployee().getEmp_name());
		}
	}

	@Override
	public List<EmployeeTraining> getAllEmployeesTrainingHistory() {

		return emptrainrepo.findAll();
	}

	@Override
	public List<EmployeeTraining> getEmployeesTrainingByEmployeeId(Long empid) {

		List<EmployeeTraining> empHistList = Optional.ofNullable(emptrainrepo.findByEmployeeId(empid))
				.orElse(Collections.emptyList());

		return empHistList;
	}

	@Override
	public int updateCompletionTime(Long id, String completion_date) {
		int result = emptrainrepo.updateCompletionTime(id, completion_date);
		if (result > 0) {
			return result;
		} else {
			throw new ResourceNotModifiedException("Completion Time is not Updated");
		}
	}

	@Override
	public Training getTrainingByHistId(Long histid) {
		EmployeeTraining employeeTrainHistoryById = emptrainrepo.getEmployeeTrainingById(histid);
		return trainserv.getTrainingById(employeeTrainHistoryById.getTraining().getTraining_id());
	}

	@Override
	public EmployeeTraining getEmployeeTrainingByID(Long emptrainhist_id) {

		return emptrainrepo.findById(emptrainhist_id)
				.orElseThrow(() -> new ResourceNotFoundException("No Employee Training history Found "));
	}

	@Override
	public List<EmployeeTrainingDto> getAllTrainingListOfAllEmployees() {

		List<EmployeeTrainingDto> trainList = new ArrayList<>();

		List<Object[]> objList = emptrainrepo.getAllTrainingsOfAllEmployees();

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

	@Override
	public List<CompetencyScore> getAllTrainingCompetenciesByEmpId(Long emp_id) {

		List<CompetencyScore> result = emptrainrepo.getTrainingAndScoreByEmployeeId(emp_id).stream()
				.map(emptrain -> {
					CompetencyScore scores = new CompetencyScore();

					scores.setName(emptrain.getTraining().getTraining_name());
					scores.setScore(emptrain.getCompetency().getScore());

					return scores;

				}).collect(Collectors.toList());
		
		result.stream().forEach(System.err::println);
		return result;

	}

	@Override	
	public int updateEmployeeTraining(EmployeeTraining emptraining) {
		
		System.err.println("Employee Training to be updated "+emptraining.toString());
//		Long obj = emptraining.getTraining_ids().stream().findFirst().get();
		Long obj  =emptraining.getTraining_ids();
		int result = 0;
		EmployeeTraining empTrainings = emptrainrepo.getTrainingByTrainingAndEmpId(emptraining.getEmployee().getEmp_id(),obj);
		
		if(empTrainings!= null) {
			System.err.println("Emptraining found for given training and emp id "+empTrainings);
			System.err.println("Call to UPDATE method");
		}
		else {
			System.err.println("Calling to save method");
		}
		return 0;
//		if(empTrainings!= null) {
//			result = emptrainrepo.updateEmployeeTrainingByEmpTrainId(empTrainings.getEmp_train_id(), emptraining.getCompetency().getCompetency_id(), emptraining.getTrainingTimeSlot().getTraining_time_slot_id());
//			if(result > 0) {
//				System.err.println("After updating the employee training "+emptrainrepo.getTrainingByTrainingAndEmpId(emptraining.getEmployee().getEmp_id(),obj).toString());
//				return result;
//			}
//			else {
//				throw new GlobalException("Training is not updated of "+emptraining.getEmployee().getEmp_name());
//			}
//		}
//		else {
//			 if(emptrainrepo.save(emptraining)!=null) {
//				 return 1;
//			 }
//			 throw new GlobalException("Training is not saved of Employee "+emptraining.getEmployee().getEmp_name());
//		}
		 
	}

}
