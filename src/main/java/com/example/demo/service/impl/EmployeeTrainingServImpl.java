package com.example.demo.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ChartDto;
import com.example.demo.dto.EmployeeTrainingDto;
import com.example.demo.entity.Competency;
import com.example.demo.entity.CompetencyScore;
import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeTraining;
import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.entity.Training;
import com.example.demo.entity.TrainingTimeSlot;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.CompetencyRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.EmployeeTrainingRepository;
import com.example.demo.service.IEmployeeTrainingHistoryService;
import com.example.demo.service.IEmployeeTrainingService;
import com.example.demo.service.ITrainingService;
import com.example.demo.service.ITrainingTimeSlotService;

@Service("emptrainserv")
public class EmployeeTrainingServImpl implements IEmployeeTrainingService {

	private final EmployeeTrainingRepository emptrainrepo;
	private final ITrainingService trainserv;
	private final IEmployeeTrainingHistoryService emptrainhistserv;
	private final EmployeeRepository emprepo;
	private final CompetencyRepository competencyrepo;
	private final ITrainingTimeSlotService traintimeslotserv;

	public EmployeeTrainingServImpl(EmployeeTrainingRepository emptrainrepo, ITrainingService trainserv,
			IEmployeeTrainingHistoryService emptrainhistserv, EmployeeRepository emprepo,
			CompetencyRepository competencyrepo, ITrainingTimeSlotService traintimeslotserv) {
		super();
		this.emptrainrepo = emptrainrepo;
		this.trainserv = trainserv;
		this.emptrainhistserv = emptrainhistserv;
		this.emprepo = emprepo;
		this.competencyrepo = competencyrepo;
		this.traintimeslotserv = traintimeslotserv;
	}

	private DateTimeFormatter dday = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private DateTimeFormatter ttime = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public EmployeeTraining saveEmployeeTraining(EmployeeTraining training) {

		Long training_ids = training.getTraining_ids();

		Training trainingObject = trainserv.getTrainingById(training_ids);

		EmployeeTraining train = new EmployeeTraining();

		Employee emp = emprepo.findById(training.getEmployee().getEmpId()).get();

		Competency competency = competencyrepo.findById(training.getCompetency().getCompetency_id()).get();

		train.setCompetency(competency);
		train.setEmployee(emp);
		train.setTraining(trainingObject);
		train.setTraining_date((training.getTraining_date()));
		train.setCompletion_date(training.getCompletion_date());
		train.setTrainingTimeSlot(training.getTrainingTimeSlot());

		EmployeeTraining savedEmpTraining = emptrainrepo.save(train);

		if (savedEmpTraining != null) {
			EmployeeTrainingHistory emptrainhist = new EmployeeTrainingHistory();
			emptrainhist.setTraining(savedEmpTraining.getTraining());
			emptrainhist.setTraining_date(savedEmpTraining.getTraining_date());
			emptrainhist.setTrainingTimeSlot(savedEmpTraining.getTrainingTimeSlot());
			emptrainhist.setEmployee(savedEmpTraining.getEmployee());
			emptrainhist.setCompetency(savedEmpTraining.getCompetency());

			emptrainhistserv.saveEmployeeTrainingHistory(emptrainhist);

			return savedEmpTraining;
		} else {
			throw new GlobalException(
					"No Training is assigned to the Employee " + training.getEmployee().getEmpName());
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
	public Training getTrainingByEmpTrainId(Long histid) {
		EmployeeTraining employeeTrainById = emptrainrepo.getEmployeeTrainingById(histid);
		return trainserv.getTrainingById(employeeTrainById.getTraining().getTraining_id());
	}

	@Override
	public EmployeeTraining getEmployeeTrainingByID(Long emptrain_id) {

		return emptrainrepo.findById(emptrain_id)
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

		List<CompetencyScore> result = emptrainrepo.getTrainingAndScoreByEmployeeId(emp_id).stream().map(emptrain -> {
			CompetencyScore scores = new CompetencyScore();

			scores.setName(emptrain.getTraining().getTraining_name());
			scores.setScore(emptrain.getCompetency().getScore());

			return scores;

		}).collect(Collectors.toList());

		return result;

	}

	@Override
	@Transactional
	public int updateEmployeeTraining(EmployeeTraining emptraining) {

		Competency compet = competencyrepo.findById(emptraining.getCompetency().getCompetency_id()).get();

		TrainingTimeSlot traintimeslot = traintimeslotserv
				.getTrainingTimeSlotById(emptraining.getTrainingTimeSlot().getTraining_time_slot_id());

		int result = emptrainrepo.updateEmployeeTrainingByEmpTrainId(emptraining.getEmp_train_id(),
				compet.getCompetency_id(), traintimeslot.getTraining_time_slot_id(), emptraining.getTraining_date(),
				emptraining.getTraining_date());
		if (result > 0) {
			EmployeeTraining savedEmpTraining = emptrainrepo.getEmployeeTrainingById(emptraining.getEmp_train_id());

			EmployeeTrainingHistory emptrainhist = new EmployeeTrainingHistory();

			emptrainhist.setTraining(savedEmpTraining.getTraining());
			emptrainhist.setTraining_date(savedEmpTraining.getTraining_date());
			emptrainhist.setTrainingTimeSlot(savedEmpTraining.getTrainingTimeSlot());
			emptrainhist.setEmployee(savedEmpTraining.getEmployee());
			emptrainhist.setCompetency(savedEmpTraining.getCompetency());

			emptrainhistserv.saveEmployeeTrainingHistory(emptrainhist);

			return result;
		} else {
			throw new GlobalException("Training is not updated of the Employee");
		}

	}

	@Override
	public EmployeeTraining getEmployeesTrainingByEmployeeIdAndTrainingId(Long empid, Long training_id) {
		EmployeeTraining obj = emptrainrepo.getTrainingByTrainingAndEmpId(empid, training_id);

		if (obj != null) {
			return obj;
		} else {
			throw new ResourceNotFoundException("No Trainings found");
		}
	}

	@Override
	public List<ChartDto> getAllEmployeesTrainingForCharts() { 
		
		List<Object[]> rows = emptrainrepo.getAllTrainingsWithCount();
		
		List<ChartDto> result = new ArrayList<>();

        for (Object[] row : rows) {
            String trainingName = (String) row[0];
            // row[1] = totalEmployees (we won’t use directly in chart)
            Long comp25 = ((Number) row[2]).longValue();
            Long comp50 = ((Number) row[3]).longValue();
            Long comp75 = ((Number) row[4]).longValue();
            Long comp100 = ((Number) row[5]).longValue();

            result.add(new ChartDto(trainingName, comp25, comp50, comp75, comp100)); 
        }
        
        return result;
	}

	@Override
	public int countTrainingByEmpId(Long emp_id) {

		Employee emp = emprepo.findById(emp_id).get();
		return emptrainrepo.countByEmployee(emp);		
	}	 

}
