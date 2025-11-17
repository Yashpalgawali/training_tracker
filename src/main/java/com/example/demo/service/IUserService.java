package com.example.demo.service;

import com.example.demo.entity.Users;

public interface IUserService {

	public int updateUserPassword(Users user);
	
	public Users getUserByEmail(String email);
}
