
package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeTrainingHistory;
import com.example.demo.entity.Training;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.DesignationRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.EmployeeTrainingHistoryRepository;
import com.example.demo.repository.TrainingRepository;
import com.example.demo.service.IEmployeeService;
import com.example.demo.service.IEmployeeTrainingHistoryService;

@Service("empserv")
public class EmployeeServImpl implements IEmployeeService {

	private final EmployeeRepository emprepo;
	private final DesignationRepository desigrepo;
	private final DepartmentRepository deptrepo;
	private final TrainingRepository trainRepository;
	private final EmployeeTrainingHistoryRepository emptrainhistrepo;
	private final IEmployeeTrainingHistoryService emptrainhistserv; 

	/**
	 * @param emprepo
	 * @param desigrepo
	 * @param deptrepo
	 * @param trainRepository
	 * @param emptrainhistrepo
	 * @param emptrainhistserv
	 */
	public EmployeeServImpl(EmployeeRepository emprepo, DesignationRepository desigrepo, DepartmentRepository deptrepo,
			TrainingRepository trainRepository, EmployeeTrainingHistoryRepository emptrainhistrepo,
			IEmployeeTrainingHistoryService emptrainhistserv) {
		super();
		this.emprepo = emprepo;
		this.desigrepo = desigrepo;
		this.deptrepo = deptrepo;
		this.trainRepository = trainRepository;
		this.emptrainhistrepo = emptrainhistrepo;
		this.emptrainhistserv = emptrainhistserv;
	}

	private Logger logger = LoggerFactory.getLogger(getClass());

	private DateTimeFormatter dday = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private DateTimeFormatter ttime = DateTimeFormatter.ofPattern("HH:mm:ss");	
	
	@Override
	public Employee saveEmployee(Employee emp) {

		List<Long> trainingList = emp.getTraining_ids();
		logger.info("TRAINIG ID's are {} ",trainingList);
		Employee savedEmployee = emprepo.save(emp);
		if(savedEmployee!=null)
		{
			return savedEmployee;
		}
		else {
			throw new GlobalException("Employee " + emp.getEmp_name() + " is not saved and No trainings are provided");
		}
	}

	@Override
	public Employee getEmployeeByEmployeeId(Long empid) {

		return emprepo.findById(empid)
				.orElseThrow(() -> new ResourceNotFoundException("No Employee found for given ID " + empid));
	}

	@Override
	public int updateEmployee(Employee emp) {

		Employee foundEmp = getEmployeeByEmployeeId(emp.getEmp_id());

		int result = emprepo.updateEmployee(emp.getEmp_id(), emp.getEmp_name(), emp.getEmp_code(), emp.getJoining_date(), emp.getDepartment().getDept_id(), emp.getDesignation().getDesig_id());
 
		if(result > 0) {
			emp.getTraining_ids().stream().map( training-> {
				
				EmployeeTrainingHistory history = new EmployeeTrainingHistory();
				
				history.setEmployee(emp);
				history.setTraining(trainRepository.findById(training).get());
				history.setTraining_date(dday.format(LocalDateTime.now()));
				
				emptrainhistrepo.save(history);
				
				return history;
				
			});
			return result;
		}
		else {
			throw new ResourceNotModifiedException("Employee " + emp.getEmp_name() + " is not updated");
		}

	}

	@Override
	public List<Employee> getAllEmployees() {
		var elist = emprepo.findAll();
		
		if (elist.size() > 0) {
			return elist;
		} else {
			throw new ResourceNotFoundException("No Employees found !!!");
		}
	}

	@Override
	public Employee getEmployeeByEmployeeCode(String empcode) {
		var emp = emprepo.getEmployeeByCode(empcode);
		if (emp != null) {
			return emp;
		} else {
			throw new ResourceNotFoundException("No Employee found for given Employee Code " + empcode);
		}
	}

	@Override
	public List<Training> getAllTrainingsByEmployeeId(Long empid) {

//		List<Training> trainList = emprepo.getAllTrainingsByEmployeeId(empid);
//		return trainList;
		return null;
	} 

}
