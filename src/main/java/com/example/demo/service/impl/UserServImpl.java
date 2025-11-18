package com.example.demo.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Users;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.IUserService;

@Service("userserv")
public class UserServImpl implements IUserService {

	private final UsersRepository userrepo;

	private final BCryptPasswordEncoder passEncoder;

	public UserServImpl(UsersRepository userrepo, BCryptPasswordEncoder passEncoder) {
		super();
		this.userrepo = userrepo;
		this.passEncoder = passEncoder;
	}

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
		else {
			
		}
		
		if(result>0 ) {
			return result;
		}
		throw new ResourceNotModifiedException("Password is not updated");
	}

	@Override
	public Users getUserByEmail(String email) {
		return userrepo.getUserByEmailId(email);
	}

}
