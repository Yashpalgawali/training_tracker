package com.example.demo.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ErrorResponseDto;
import com.example.demo.dto.ResponseDto;
import com.example.demo.entity.Users;
import com.example.demo.service.IEmailService;
import com.example.demo.service.IOtpService;
import com.example.demo.service.IUserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("password")
public class ChangePasswordController {

	private final IUserService userserv;
	private final IOtpService otpserv;
	private final IEmailService emailserv;

	public ChangePasswordController(IUserService userserv, IOtpService otpserv, IEmailService emailserv) {
		super();
		this.userserv = userserv;
		this.otpserv = otpserv;
		this.emailserv = emailserv;
	}

	@PutMapping("/change")
	public ResponseEntity<ResponseDto> changePassword(@RequestBody Users user) {

		userserv.updateUserPassword(user);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseDto(HttpStatus.OK.toString(), "Password Changed Successfully"));
	}

	@GetMapping("/otp/{vemail}")
	public ResponseEntity<?> otpForForgotPassword(@PathVariable String vemail, HttpSession sess) {
		if (userserv.getUserByEmail(vemail) != null) {
			otpserv.generateotp(vemail);
			int otp = otpserv.getOtp(vemail);
			sess.setAttribute("vemail", vemail);
			sess.setAttribute("otp", otp);
			emailserv.sendSimpleEmail(vemail, "Respected Sir/Ma'am, \n\t Your OTP to change the password is " + otp,
					"OTP for confirmation");
			return new ResponseEntity<String>("" + otpserv.getOtp(vemail), HttpStatus.OK);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("", HttpStatus.NOT_FOUND,
					"No User found for given Email ID " + vemail, LocalDateTime.now().toString()));
		}
	}

	@GetMapping("/email/{vemail:.+}/otp/{otp}")
	public ResponseEntity<?> validateOtpForForgotPassword(@PathVariable String vemail, @PathVariable int otp,
			HttpSession sess) {

		int ootp = otpserv.getOtp(vemail);

		if (!vemail.equals("") && (ootp == otp)) {

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(), "Otp Matched"));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto("",
				HttpStatus.INTERNAL_SERVER_ERROR, "OTP did not matched", LocalDateTime.now().toString()));

	}

	@PutMapping("/forgot")
	public ResponseEntity<ResponseDto> forgotPasswordChange(@RequestBody Users user) {
		userserv.updateUserPassword(user);

		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseDto(HttpStatus.OK.toString(), "Password Changed Successfully! Please Log in to continue."));
	}
}
