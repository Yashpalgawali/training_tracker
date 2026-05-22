package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Users;

public interface IUserService {

	public int updateUserPassword(Users user);

	public Users getUserByEmail(String email);

	public void createUser(Users user);

	public List<Users> getAllUsers();
	
	public void updateUser(Users user);
	
	public Users getUserById(Long id);
	
	public void updateUserStatusById(Long id , int enabled);
}
