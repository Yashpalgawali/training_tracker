package com.example.demo.security;

import java.io.IOException;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	 @Override
	    public void commence(
	            HttpServletRequest request,
	            HttpServletResponse response,
	            AuthenticationException authException) throws IOException {

	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.setContentType("application/json");

	        String message = (authException instanceof DisabledException)
	                ? "Your account has been disabled. Please contact the administrator."
	                : "Invalid username or password";

	        response.getWriter().write("{\"message\": \"" + message + "\"}");
	        response.getWriter().flush();
	    }
}
