package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.EmployeeTrainingHistory;

@Repository("emptrainhistrepo")
public interface EmployeeTrainingHistoryRepository extends JpaRepository<EmployeeTrainingHistory, Long> {

	@Query(value= "SELECT et.* FROM tbl_employee_training_history et "
			+ " JOIN tbl_employee e ON et.emp_id=e.emp_id "
			+ " JOIN tbl_training t ON t.training_id=et.training_id "
			+ " WHERE et.emp_id=:empid",nativeQuery = true)
//	@Query("SELECT et FROM EmployeeTrainingHistory et JOIN et.employee JOIN et.training WHERE et.employee.emp_id=:empid")
	List<EmployeeTrainingHistory> findByEmployeeId(Long empid);
	
	
//	List<EmployeeTrainingHistory> findByEmployee(Employee employee);

	@Query("UPDATE EmployeeTrainingHistory eth SET eth.completion_date=:completion_date WHERE eth.emp_train_hist_id=:id")
	@Modifying
	@Transactional
	public int updateCompletionTime(Long id,String completion_date);
	
	@Query("SELECT eth FROM EmployeeTrainingHistory eth WHERE eth.emp_train_hist_id=:emp_train_hist_id")
	EmployeeTrainingHistory getEmployeeTrainHistoryById(Long emp_train_hist_id);

	@Query(value ="SELECT emp.emp_name,train.training_name,emptrain.training_date,emptrain.completion_date,desig.desig_name,dept.dept_name,company.comp_name FROM tbl_employee_training_history AS emptrain JOIN tbl_employee AS emp ON emp.emp_id=emptrain.emp_id JOIN tbl_designation AS desig ON desig.desig_id=emp.desig_id JOIN tbl_training as train ON train.training_id=emptrain.training_id JOIN tbl_department AS dept ON dept.dept_id=emp.dept_id JOIN tbl_company AS company ON company.company_id=dept.company_id ",nativeQuery = true)
	List<Object[]> getAllTrainingsOfAllEmployees();
}	
