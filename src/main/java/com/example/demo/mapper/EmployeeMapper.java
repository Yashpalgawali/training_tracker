package com.example.demo.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Department;
import com.example.demo.entity.Designation;
import com.example.demo.entity.Employee;

public class EmployeeMapper {
	 

	public static Employee EmployeeDtoToEmployee(EmployeeDTO empdto, Employee emp) {
		// Employee emp = new Employee();

		emp.setEmpName(empdto.getEmp_name());
		emp.setEmpCode(empdto.getEmp_code());

		Department dept = new Department();
		dept.setDept_id(Long.parseLong(empdto.getDepartment()));

		Designation desig = new Designation();
		desig.setDesig_id(Long.valueOf(empdto.getDesignation()));
		
		emp.setDepartment(dept);
		emp.setDesignation(desig);
		System.err.println("EMployee in mapper "+emp.toString()); 

		return emp;
	}
}
