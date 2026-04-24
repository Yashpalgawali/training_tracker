package com.example.demo.service.impl;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Users;
import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.IUserService;

import lombok.RequiredArgsConstructor;

@Service("userserv")
@RequiredArgsConstructor
public class UserServImpl implements IUserService {

	private final UsersRepository userrepo;

	private final BCryptPasswordEncoder passEncoder;
	
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
		userrepo.save(user);		
	}

	@Override
	public List<Users> getAllUsers() {
		var userList = userrepo.findAll();
		if(userList.size() > 0) {
			throw new ResourceNotFoundException("No user(s) found");
		}
		return userList;
	}

}
