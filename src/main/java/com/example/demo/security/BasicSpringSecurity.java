package com.example.demo.security;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

//@Configuration
public class BasicSpringSecurity {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((auth) -> {
			
			auth.anyRequest().authenticated();
			
		});
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		http.httpBasic(basic -> {});
		http.csrf(csrf->{csrf.disable();});
		
		http.cors(cors->{
			cors.configurationSource(config -> {
				CorsConfiguration conf = new CorsConfiguration();
				conf.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
				conf.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://localhost:4200"));
				conf.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
				return conf;
			});
		});
		http.headers(header->{
			header.frameOptions(frame->frame.sameOrigin());			
		});
		return http.build();
	}
	
//	@Bean
//	UserDetailsService userDetailService() {
//		var user = User.withUsername("in28minutes").password("{noop}dummy").roles("USER").build();
//		
//		var admin = User.withUsername("admin").password("{noop}dummy").roles("USER","ADMIN").build();
//		return new InMemoryUserDetailsManager(user,admin);
//	}
	
	@Bean
	DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
				.build() ;
	}
	
	@Bean
	UserDetailsService userDetailService(DataSource dataSource) {
		var user = User.withUsername("in28minutes")
					//.password("{noop}dummy")
					.password("dummy")
					.passwordEncoder(str -> passwordEncoder().encode(str))
					.roles("USER").build();
		
		var admin = User.withUsername("admin")
					.password("dummy")
					.passwordEncoder(str -> passwordEncoder().encode(str))
					.roles("USER","ADMIN").build();
		//return new InMemoryUserDetailsManager(user,admin);
		var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		jdbcUserDetailsManager.createUser(user);
		jdbcUserDetailsManager.createUser(admin);
		
		return jdbcUserDetailsManager;
	}
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
