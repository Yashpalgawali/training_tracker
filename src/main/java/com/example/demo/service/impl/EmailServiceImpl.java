package com.example.demo.service.impl;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.service.IEmailService;

@Service("emailserv")
public class EmailServiceImpl implements IEmailService {

	private JavaMailSender mailsend;
	
	private Environment env; 
		 
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
	 
	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	  
	@Override
	public void sendSimpleEmail(String toemail, String body, String subject) {
		String from = env.getProperty("spring.mail.username");
		
		logger.info("Sending email to: {}, Subject: {}", toemail, subject);
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(toemail);
		message.setFrom(from);
		message.setSubject(subject);
		message.setText(body);
		System.err.println("Mail is "+message.toString());
		try {
			mailsend.send(message);
		}
		catch(Exception e)
		{
			System.err.println("mail sent failed");
		}
		
	}
}
