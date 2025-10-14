package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Employee;

@Repository("emprepo")
public interface EmployeeRepository extends JpaRepository<Employee, Long>  {

	@Query("UPDATE Employee e SET e.empName=:empname,e.empCode=:empcode,e.joiningDate=:doj,e.department.dept_id=:deptid,"
			+ "e.contractorName=:contractor,e.category.category_id=:category_id,e.designation.desig_id=:desigid  WHERE e.empId=:eid")
	@Modifying
	public int updateEmployee(Long eid,String empname,String empcode,String contractor,Long category_id,String doj,Long deptid,Long desigid);

	@Query("SELECT e FROM Employee e JOIN e.department JOIN e.designation WHERE e.empCode=:emp_code")
	public Employee getEmployeeByCode(String emp_code);

	@Query("SELECT e FROM Employee e WHERE e.empName=:emp_name")
 	public Optional<Employee>findByEmp_name(String emp_name);

	 
	Page<Employee> findByEmpNameContainingIgnoreCaseOrEmpCodeContainingIgnoreCase(
	        String empName, String empCode, Pageable pageable);

//	@Query("SELECT e.training FROM Employee e WHERE e.emp_id=:empid")
//	public List<Training> getAllTrainingsByEmployeeId(Long empid);

//	@Query("SELECT new com.example.demo.dto.EmployeeDTO(e.emp_id, e.emp_name, e.emp_code,e.joining_date,e.contractor_name,dept.dept_name,"
//			+ "dept.company.comp_name,desig.desig_name,cat.category) "
//			+ "FROM Employee e JOIN e.department dept JOIN e.designation desig JOIN e.category cat")
//	List<EmployeeDTO> findAllEmployeeDtos();
}