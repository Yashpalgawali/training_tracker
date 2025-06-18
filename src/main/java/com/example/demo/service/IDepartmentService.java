package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Department;

public interface IDepartmentService {

	public Department saveDepartment(Department dept);	
	public List<Department> getAllDepartments();	
	public Department getDepartmentByDeptId(Long deptid);
	public int updateDepartment(Department dept);
	
	public List<Department> getAllDepartmentsByCompanyId(Long compid);
}
