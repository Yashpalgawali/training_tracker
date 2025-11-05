package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Activity;
import com.example.demo.entity.Company;
import com.example.demo.entity.Department;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.ActivityRepository;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.service.IDepartmentService;

@Service("deptserv")
public class DepartmentServImpl implements IDepartmentService {

	private final DepartmentRepository deptrepo;

	private final ActivityRepository activityrepo;

	public DepartmentServImpl(DepartmentRepository deptrepo, ActivityRepository activityrepo) {
		super();
		this.deptrepo = deptrepo;
		this.activityrepo = activityrepo;
	}

	// Define a custom format pattern
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public Department saveDepartment(Department dept) {
		var department = deptrepo.save(dept);

		if (department != null) {
			Activity activity = Activity.builder().activity("Department "+department.getDeptName()+" is saved successfully").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			return department;
		} else {
			Activity activity = Activity.builder().activity("Department "+dept.getDeptName()+" is not saved ").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			throw new GlobalException("Department " + dept.getDeptName() + " is not saved ");
		}
	}

	@Override
	public List<Department> getAllDepartments() {

		List<Department> deptList = deptrepo.findAll();
		if (deptList.size() > 0)
			return deptList;
		else {
			throw new ResourceNotFoundException("No Departments Found");
		}
	}

	@Override
	public Department getDepartmentByDeptId(Long deptid) {
		return deptrepo.findById(deptid)
				.orElseThrow(() -> new ResourceNotFoundException("No Department found for given ID " + deptid));
	}

	@Override
	public int updateDepartment(Department dept) {
		int res = deptrepo.updateDepartment(dept.getDeptId(), dept.getDeptName(), dept.getCompany().getCompanyId());
		if (res > 0) {
			Activity activity = Activity.builder().activity("Department "+dept.getDeptName()+" is updated successfully").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			return res;
		} else {
			Activity activity = Activity.builder().activity("Department "+dept.getDeptName()+" is not updated ").activityDate(dateFormatter.format(LocalDateTime.now()) ).activityTime(timeFormatter.format(LocalDateTime.now())).build();
			activityrepo.save(activity);
			throw new ResourceNotModifiedException("Department " + dept.getDeptName() + " is not updated ");
		}
	}

	@Override
	public List<Department> getAllDepartmentsByCompanyId(Long compid) {

		Company comp = new Company();
		comp.setCompanyId(compid);
		var dlist = deptrepo.findByCompany(comp);
		if (dlist.size() > 0) {
			return dlist;
		} else {
			throw new ResourceNotFoundException("No Departments found for given Company");
		}
	}

	@Override
	public Department getDepartmentByDeptName(String dname) {

		return deptrepo.findByDeptName(dname);
	}

	@Override
	public Department getDepartmentByDeptNameAndCompName(String dname, String cname) {

		return deptrepo.getDepartmentByDeptNameAndCompanyName(dname, cname);
	}

}
