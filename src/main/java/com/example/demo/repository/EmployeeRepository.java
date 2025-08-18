package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Employee;

@Repository("emprepo")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query("UPDATE Employee e SET e.emp_name=:empname,e.emp_code=:empcode,e.joining_date=:doj,e.department.dept_id=:deptid,"
			+ "e.contractor_name=:contractor,e.category.category_id=:category_id,e.designation.desig_id=:desigid  WHERE e.emp_id=:eid")
	@Modifying
	public int updateEmployee(Long eid,String empname,String empcode,String contractor,Long category_id,String doj,Long deptid,Long desigid);

	@Query("SELECT e FROM Employee e JOIN e.department JOIN e.designation WHERE e.emp_code=:emp_code")
	public Employee getEmployeeByCode(String emp_code);

	@Query("SELECT e FROM Employee e   WHERE e.emp_name=:emp_name")
 	public Optional<Employee>findByEmp_name(String emp_name);

//	@Query("SELECT e.training FROM Employee e WHERE e.emp_id=:empid")
//	public List<Training> getAllTrainingsByEmployeeId(Long empid);

}
