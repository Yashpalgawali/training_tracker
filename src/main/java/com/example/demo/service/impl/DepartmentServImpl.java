package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Company;
import com.example.demo.entity.Department;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.service.IDepartmentService;

@Service("deptserv")
public class DepartmentServImpl implements IDepartmentService {

	private final DepartmentRepository deptrepo;

	/**
	 * @param deptrepo
	 */
	public DepartmentServImpl(DepartmentRepository deptrepo) {
		super();
		this.deptrepo = deptrepo;
	}

	@Override
	public Department saveDepartment(Department dept) {
		var department = deptrepo.save(dept);
		
		if(department!=null) {
			return department;
		}
		else {
			throw new GlobalException("Department "+dept.getDept_name()+" is not saved ");
		}
	}

	@Override
	public List<Department> getAllDepartments() {
		 
		List<Department> deptList = deptrepo.findAll();
		if(deptList.size()>0)
			return deptList;
		else {
			throw new ResourceNotFoundException("No Departments Found");
		}
	}

	@Override
	public Department getDepartmentByDeptId(Long deptid) {
		return deptrepo.findById(deptid).orElseThrow(() -> new ResourceNotFoundException("No Department found for given ID "+deptid));		
	}

	@Override
	public int updateDepartment(Department dept) {
		int res = deptrepo.updateDepartment(dept.getDept_id(), dept.getDept_name(), dept.getCompany().getCompany_id());
		if(res>0) {
			return res;
		}
		else {
			throw new ResourceNotModifiedException("Department "+dept.getDept_name()+" is not updated ");
		}
	}

	@Override
	public List<Department> getAllDepartmentsByCompanyId(Long compid) {
		 
		Company comp = new Company();
		comp.setCompany_id(compid);
		var dlist = deptrepo.findByCompany(comp);
		if(dlist.size()>0) {
			return dlist;
		}
		else {
			throw new ResourceNotFoundException("No Departments found for given Company");
		}
	}

	@Override
	public Department getDepartmentByDeptName(String dname) {
		
		return deptrepo.getDepartmentByDeptName(dname);
	}

	@Override
	public Department getDepartmentByDeptNameAndCompName(String dname, String cname) {
		 
		return getDepartmentByDeptNameAndCompName(dname, cname);
	}

}
