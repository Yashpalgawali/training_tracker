package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Activity;
import com.example.demo.entity.Users;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.ActivityRepository;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.IUserService;

import lombok.RequiredArgsConstructor;

@Service("userserv")
@RequiredArgsConstructor
public class UserServImpl implements IUserService {

	private final UsersRepository userrepo;

	private final BCryptPasswordEncoder passEncoder;
	
	private final ActivityRepository activityrepo;

	private DateTimeFormatter tday = DateTimeFormatter.ofPattern("dd-mm-yyyy");
	private DateTimeFormatter ttime = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	@Override
	@Transactional	
	public int updateUserPassword(Users user) {

		String password = user.getPassword();
		
		String encryptedPass = passEncoder.encode(password);
		
		Long id = user.getUser_id();
	
		String email = user.getEmail();
		int result =0;
		if(id!=null)
		{
			result = userrepo.updateUsersPassword(encryptedPass, id);
		}
		else if(!email.equals(""))
		{
			result = userrepo.updateUsersPasswordByEmail(encryptedPass, email); 
		}
		else {		}
		
		if(result > 0 ) {
			return result;
		}
		throw new ResourceNotModifiedException("Password is not Updated");
	}

	@Override
	public Users getUserByEmail(String email) {
		return userrepo.getUserByEmailId(email);
	}

	@Override
	public void createUser(Users user) {
		Users foundUser = userrepo.getUserByEmailId(user.getEmail());
		
		if(foundUser!=null) {
			throw new ResourceAlreadyExistsException("user","email",user.getEmail());
		}
		String encryptedPass = passEncoder.encode(user.getPassword());
		user.setPassword(encryptedPass);
		Users savedUser = userrepo.save(user);
		
		Activity activity = new Activity();
		activity.setActivity("User "+user.getUsername()+" is created successfully");
		activity.setActivityDate(tday.format(LocalDateTime.now()));
		activity.setActivityTime(ttime.format(LocalDateTime.now()));
		activityrepo.save(activity);
		
		if(savedUser == null) {
			throw new GlobalException("The user "+user.getUsername()+" is not created");
		}
	}

	@Override
	public List<Users> getAllUsers() {
		var userList = userrepo.findAll();
		
		if(userList.size() > 0) {			
			return userList;
		}		
		throw new ResourceNotFoundException("No user(s) found");
	}

	@Override
	@Transactional
	public void updateUser(Users user) {
		Optional<Users> foundUser =  userrepo.findById(user.getUser_id());
		if(!foundUser.isPresent()) {
			throw new ResourceNotFoundException("No User found for given ID "+user.getUser_id());			
		}
		
		String encryptedPass = passEncoder.encode(user.getPassword());
		user.setPassword(encryptedPass);
		
		int result =userrepo.updateUser(user.getUser_id(), user.getUsername(), encryptedPass, user.getEnabled(), user.getRole(), user.getEmail());
		if(result >0 ) {
			Activity activity = new Activity();
			activity.setActivity("User "+user.getUsername()+" is updated successfully");
			activity.setActivityDate(tday.format(LocalDateTime.now()));
			activity.setActivityTime(ttime.format(LocalDateTime.now()));
			activityrepo.save(activity);			
		}
		else {
			Activity activity = new Activity();
			activity.setActivity("User "+user.getUsername()+" is not updated ");
			activity.setActivityDate(tday.format(LocalDateTime.now()));
			activity.setActivityTime(ttime.format(LocalDateTime.now()));
			activityrepo.save(activity);
			throw new ResourceNotModifiedException("User "+user.getUsername()+" is not modified");
		}
	}

	@Override
	public Users getUserById(Long id) {
		Optional<Users> foundUser = userrepo.findById(id);
		if(foundUser.isPresent()) {
			return foundUser.get();
		}
		throw new ResourceNotFoundException("No user found for given ID "+id);
	}

	@Override
	@Transactional
	public void updateUserStatusById(Long id, int enabled) {
		int res = userrepo.updateUserStatusById(id, enabled);
		if(res < 0 ) {
			throw new ResourceNotModifiedException("User status is not updated");
		}
	}

}
