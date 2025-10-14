package com.example.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ChartDto;
import com.example.demo.entity.EmployeeTraining;

@Repository("emptrainrepo")
public interface EmployeeTrainingRepository extends JpaRepository<EmployeeTraining, Long> {

	@Query(value= "SELECT et.* FROM tbl_employee_training et "
			+ " JOIN tbl_employee e ON et.emp_id=e.emp_id "
			+ " JOIN tbl_training t ON t.training_id=et.training_id "
			+"  JOIN tbl_training_time_slot sl ON sl.training_time_slot_id=et.training_time_slot_id"
		 
			+ " WHERE et.emp_id=:empid",nativeQuery = true)
//	@Query("SELECT et FROM EmployeeTraining et JOIN et.employee JOIN et.training JOIN et.trainingTimeSlot WHERE et.employee.emp_id=:empid")
	List<EmployeeTraining> findByEmployeeId(Long empid);

	@Query("UPDATE EmployeeTraining eth SET eth.completion_date=:completion_date WHERE eth.emp_train_id=:id")
	@Modifying
	@Transactional
	public int updateCompletionTime(Long id,String completion_date);
	
	@Query("SELECT eth FROM EmployeeTraining eth WHERE eth.emp_train_id=:emp_train_id")
	EmployeeTraining getEmployeeTrainingById(Long emp_train_id);

	@Query(value ="SELECT emp.emp_name,train.training_name,emptrain.training_date,emptrain.completion_date,"
			+ "desig.desig_name,dept.dept_name,company.comp_name,competency.score FROM tbl_employee_training AS emptrain "
			+ "JOIN tbl_employee AS emp ON emp.emp_id=emptrain.emp_id "
			+ "JOIN tbl_designation AS desig ON desig.desig_id=emp.desig_id "
			+ "JOIN tbl_training as train ON train.training_id=emptrain.training_id "
			+ "JOIN tbl_department AS dept ON dept.dept_id=emp.dept_id "
			+ "JOIN tbl_company AS company ON company.company_id=dept.company_id "
			+ "JOIN tbl_competency AS competency ON competency.competency_id=emptrain.competency_id ",nativeQuery = true)
	List<Object[]> getAllTrainingsOfAllEmployees();
	
	 
	@Query("SELECT eth FROM EmployeeTraining eth WHERE eth.employee.empId=:empid")
	List<EmployeeTraining> getTrainingAndScoreByEmployeeId(Long empid);
	
	@Query("SELECT eth FROM EmployeeTraining eth WHERE eth.training.training_id=:tid")
	List<EmployeeTraining> getEmployeeTrainingByTrainingId(Long tid);
	
	@Query("SELECT eth FROM EmployeeTraining eth WHERE eth.training.training_id=:training_id AND eth.employee.empId=:empid")
	public EmployeeTraining getTrainingByTrainingAndEmpId(Long empid,Long training_id);
		
	@Query("UPDATE EmployeeTraining et SET et.competency.competency_id=:competencyid,et.trainingTimeSlot.training_time_slot_id=:timeslotid,"
			+ " et.training_date=:training_date,et.completion_date=:completion_date"
			+ " WHERE et.emp_train_id=:emptrainid")
	@Modifying(clearAutomatically = true)
	@Transactional
	public int updateEmployeeTrainingByEmpTrainId(Long emptrainid, Long competencyid,Long timeslotid,String training_date,String completion_date);
 
	@Query(value="SELECT \r\n"
			+ "    t.training_name,\r\n"
			+ "    COUNT(DISTINCT et.emp_id) AS totalEmployees,\r\n"
			+ "    SUM(CASE WHEN et.competency_id = 2 THEN 1 ELSE 0 END) AS comp25,\r\n"
			+ "    SUM(CASE WHEN et.competency_id = 3 THEN 1 ELSE 0 END) AS comp50,\r\n"
			+ "    SUM(CASE WHEN et.competency_id = 4 THEN 1 ELSE 0 END) AS comp75,\r\n"
			+ "    SUM(CASE WHEN et.competency_id = 5 THEN 1 ELSE 0 END) AS comp100\r\n"
			+ "FROM tbl_employee_training et\r\n"
			+ "JOIN tbl_training t ON et.training_id = t.training_id  \r\n"
			+ "GROUP BY t.training_id;",nativeQuery = true)
	public List<Object[]> getAllTrainingsWithCount();

//	@Query(value = """
//            SELECT new com.example.demo.dto.ChartDTO(
//                t.name,
//                COUNT(DISTINCT et.emp_id),
//                SUM(CASE WHEN et.competency_id = 2 THEN 1 ELSE 0 END),
//                SUM(CASE WHEN et.competency_id = 3 THEN 1 ELSE 0 END),
//                SUM(CASE WHEN et.competency_id = 4 THEN 1 ELSE 0 END),
//                SUM(CASE WHEN et.competency_id = 5 THEN 1 ELSE 0 END)
//            )
//            FROM tbl_employee_training et
//            JOIN tbl_training t ON et.training_id = t.training_id
//            GROUP BY t.training_id 
//            """,nativeQuery = true)
//	public List<ChartDto> getAllTrainingsWithCount();
}	
