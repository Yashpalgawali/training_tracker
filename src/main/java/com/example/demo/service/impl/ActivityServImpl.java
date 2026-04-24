package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Activity;
import com.example.demo.exception.GlobalException;
import com.example.demo.repository.ActivityRepository;
import com.example.demo.service.IActivityService;

import lombok.RequiredArgsConstructor;

@Service("activityserv") @RequiredArgsConstructor
public class ActivityServImpl implements IActivityService {

	private final ActivityRepository activityrepo;
	
	@Override
	public List<Activity> getAllActivities() {
		List<Activity> actlist = activityrepo.findAll();
		if(actlist.size() > 0)
			return actlist;
		throw new GlobalException("No Activities found");
	}

}
