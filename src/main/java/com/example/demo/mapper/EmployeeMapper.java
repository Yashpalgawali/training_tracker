package com.example.demo.mapper;

import com.example.demo.dto.EmployeeDTO;
import com.example.demo.entity.Department;
import com.example.demo.entity.Designation;
import com.example.demo.entity.Employee;

public class EmployeeMapper {
	 

	public static Employee EmployeeDtoToEmployee(EmployeeDTO empdto, Employee emp) {
		// Employee emp = new Employee();

		emp.setEmpName(empdto.getEmpName());
		emp.setEmpCode(empdto.getEmpCode());

		Department dept = new Department();
		dept.setDept_id(Long.parseLong(empdto.getDepartment()));

		Designation desig = new Designation();
		desig.setDesigId(Long.valueOf(empdto.getDesignation()));

		emp.setDepartment(dept);
		emp.setDesignation(desig);
		System.err.println("EMployee in mapper "+emp.toString()); 

		return emp;
	}
	
	public static EmployeeDTO EmployeeToEmployeeDTO(Employee emp , EmployeeDTO empdto) {
		
		System.err.println("EMployee Object "+emp.toString());
//		empdto.setEmp_id(emp.getEmpId());
//		empdto.setEmp_name(emp.getEmpName());
//		empdto.setEmp_code(emp.getEmpCode());
//		empdto.setContractor_name(emp.getContractorName());
//		empdto.setJoining_date(emp.getJoiningDate());
//		empdto.setDepartment(emp.getDepartment().getDept_name());
//		empdto.setDesignation(emp.getDesignation().getDesigName());
//		empdto.setCompany(emp.getDepartment().getCompany().getComp_name());
//		
		return empdto;
	}
}
