package com.example.demo.service.impl;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.service.IEmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service("emailserv")
@RequiredArgsConstructor
public class EmailServiceImpl implements IEmailService {

	private final JavaMailSender mailsend;

	private final Environment env;

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Override
	public void sendSimpleEmail(String toemail, String nbody, String subject) {

		String from = env.getProperty("spring.mail.username");

		String body = """
				<!DOCTYPE html>
				<html>
				<head>
				    <style>
				        body{
				            font-family: Arial, Helvetica, sans-serif;
				            background-color:#f4f4f4;
				            margin:0;
				            padding:20px;
				        }
				        .container{
				            max-width:600px;
				            margin:auto;
				            background:#ffffff;
				            border-radius:8px;
				            padding:30px;
				            box-shadow:0 2px 8px rgba(0,0,0,0.1);
				        }
				        .header{
				            background:#1976d2;
				            color:white;
				            padding:15px;
				            text-align:center;
				            font-size:24px;
				            border-radius:6px 6px 0 0;
				        }
				        .content{
				            padding:20px;
				            color:#333333;
				            line-height:1.6;
				        }
				        .footer{
				            margin-top:20px;
				            font-size:12px;
				            color:#777777;
				            text-align:center;
				            border-top:1px solid #dddddd;
				            padding-top:10px;
				        }
				    </style>
				</head>
				<body>
				    <div class="container">
				        <div class="header">
				            Training Tracker
				        </div>

				        <div class="content">
				            <h3>Hello %s,</h3>

				            <p>
				                This is a notification from the
				                <strong>Training Tracker System</strong>.
				            </p>

				            <p>
				               %s.
				            </p>

				            <p>
				                If you have any questions, please contact your administrator.
				            </p>

				            <br/>

				            <p>
				                Regards,<br/>
				                Training Tracker Team
				            </p>
				        </div>

				        <div class="footer">
				            © 2026 Training Tracker. All Rights Reserved.
				        </div>
				    </div>
				</body>
				</html>
				""".formatted(toemail,nbody);

		try {

			MimeMessage mimeMessage = mailsend.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			helper.setFrom(from);
			helper.setTo(toemail);
			helper.setSubject(subject);
			helper.setText(body, true); // true = HTML

			mailsend.send(mimeMessage);

			System.out.println("Email sent successfully.");

		} catch (Exception e) {
			e.printStackTrace();
		}
//		String from = env.getProperty("spring.mail.username");
//
//		SimpleMailMessage message = new SimpleMailMessage();
//
//		message.setTo(toemail);
//		message.setFrom(from);
//		message.setSubject(subject);
//		message.setText(body);
//		System.err.println("Mail is " + message.toString());
//		try {
//			mailsend.send(message);
//		} catch (Exception e) {
//			System.err.println("mail sent failed");
//		}

	}
}
