package com.example.demo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Department;
import com.example.demo.entity.Designation;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Training;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.mapper.EmployeeMapper;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.DesignationRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.TrainingRepository;
import com.example.demo.service.IEmployeeService;

@Service("empserv")
public class EmployeeServImpl implements IEmployeeService {

	private final EmployeeRepository emprepo;
	private final DesignationRepository desigrepo;
	private final DepartmentRepository deptrepo;
	private final TrainingRepository trainRepository;

	/**
	 * @param emprepo
	 * @param desigrepo
	 * @param deptrepo
	 * @param trainRepository
	 */
	public EmployeeServImpl(EmployeeRepository emprepo, DesignationRepository desigrepo, DepartmentRepository deptrepo,
			TrainingRepository trainRepository) {
		super();
		this.emprepo = emprepo;
		this.desigrepo = desigrepo;
		this.deptrepo = deptrepo;
		this.trainRepository = trainRepository;
	}

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Employee saveEmployee(Employee emp) {

//		if (emp.getTraining_ids() != null && !emp.getTraining_ids().isEmpty()) {
//			List<Training> trains = trainRepository.findAllById(emp.getTraining_ids());
//			emp.setTraining(trains);
//		}

		Employee savedEmp = emprepo.save(emp);
		if (savedEmp != null) {
			return savedEmp;
		} else {
			throw new GlobalException("Employee " + emp.getEmp_name() + " is not saved");
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

		logger.info("Found Employee Object is {} ", foundEmp);

//		if (emp.getTraining_ids() != null || emp.getTraining_ids().size() > 0) {
//			List<Training> training = trainRepository.findAllById(emp.getTraining_ids());
//			if (foundEmp.getTraining() != null || emp.getTraining().size() > 0) {
//
//			}
//
//			emp.setTraining(training);
//		}
		logger.info("EMPloyee updation Object is {} ", emp);
		int res = emprepo.updateEmployee(emp.getEmp_id(), emp.getEmp_name(), emp.getEmp_code(), emp.getJoining_date(),
				emp.getDepartment().getDept_id(), emp.getDesignation().getDesig_id() );

		if (res > 0) {
			return res;
		} else {
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
