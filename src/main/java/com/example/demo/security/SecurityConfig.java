package com.example.demo.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChaint(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(auth-> {
			auth.anyRequest().permitAll();
		});
		http.cors(cors-> {
			cors.configurationSource(request-> {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedMethods(Arrays.asList("OPTIONS","PUT","POST","GET","PATCH","DELETE"));
				config.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://localhost:8080","http://192.168.0.219:3000"));
				config.setAllowedHeaders(Arrays.asList("Authorization","Content-Type","Cache-Control"));
				return config;
			});
		}); 
		http.csrf(csrf -> csrf.disable());
		http.httpBasic(basic->{ } );
		
		return http.build();
	}
}
 