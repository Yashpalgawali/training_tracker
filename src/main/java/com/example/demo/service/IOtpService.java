package com.example.demo.service;

public interface IOtpService {
	
	int generateotp(String uname);
	
	int getOtp(String uname);
	
	void clearOtp(String uname);
}
