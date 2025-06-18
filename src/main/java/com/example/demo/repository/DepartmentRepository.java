package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Company;
import com.example.demo.entity.Department;

@Repository("deptrepo")
public interface DepartmentRepository extends JpaRepository<Department, Long> {

	public List<Department> findByCompany(Company company);

	@Query("UPDATE Department d SET d.dept_name=:dname,d.company.company_id=:compid WHERE d.dept_id=:did")
	@Transactional
	@Modifying
	public int updateDepartment(Long did, String dname, Long compid);
}
