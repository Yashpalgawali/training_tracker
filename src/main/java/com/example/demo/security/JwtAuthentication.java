package com.example.demo.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
//import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import jakarta.servlet.http.HttpServletResponse;


@Configuration
public class JwtAuthentication {
 	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.httpBasic(basic->{});
		http.authorizeHttpRequests(auth -> {
		
			auth.requestMatchers("/users/**","/authenticate","/error").permitAll();
			auth.anyRequest().authenticated();
		});
		 
		http.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		 
	 	http.csrf(csrf-> csrf.disable());
	 	http.cors(cors->{
	 		cors.configurationSource(request->{
	 			 CorsConfiguration config = new CorsConfiguration();

//	 			config.setAllowedOrigins(Arrays.asList("http://13.201.9.193")); // Your Angular app's URL
	 			 config.setAllowedOrigins(Arrays.asList("http://192.168.0.*:8080","http://192.168.0.219:3000","http://localhost:8080","http://192.168.0.219:8080","http://localhost:8081","http://192.168.0.219:8081","http://localhost:3000","http://192.168.0.*:3000")); // Your React app's URL
//	 			 config.setAllowedOrigins(Arrays.asList("*")); // Your Angular app's URL
	             config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
//	             config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
	             config.setAllowedHeaders(Arrays.asList("*"));
	             config.setAllowCredentials(true); // Allow cookies
	             return config;
	 		});   
	 	});
	 	http.logout(logout->{
			logout.logoutUrl("/logouturl");
			logout.invalidateHttpSession(true);
			logout.clearAuthentication(true);
			logout.deleteCookies("SESSION");
			logout.logoutSuccessHandler((request, response , authentication)->{
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().write("{\"message\": \" Logged Out Successfully \" }");
					response.getWriter().flush();
				});
			});
		http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);	

		return http.build();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	    return config.getAuthenticationManager();
	}
	
//	@Autowired
//	public void configAuthentication(AuthenticationManagerBuilder authBuilder) throws Exception {
//		authBuilder.jdbcAuthentication()
//		.dataSource(dataSource)
//		
//		//Following will select the username from database depending on the username from Login form
//		.usersByUsernameQuery("SELECT username,password,enabled FROM tbl_users WHERE username=? ")
//		
//		//Following will select the authority(s) depending on the username
//		.authoritiesByUsernameQuery("SELECT username,role FROM tbl_users WHERE username=?")
//		
//		.passwordEncoder(new BCryptPasswordEncoder());
//	}
	
	@Bean
	KeyPair keyPair() {
	       KeyPairGenerator keypairgenerator = null;
		try {
			keypairgenerator = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	       keypairgenerator.initialize(2048);
	       return keypairgenerator.generateKeyPair();
	}

	@Bean 
	RSAKey rsaKey(KeyPair keyPair) {
		return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic()).privateKey(keyPair.getPrivate())
			.keyID(UUID.randomUUID().toString())
			.build();
	}

	@Bean
	JWKSource<SecurityContext> jwkSource(RSAKey rsaKey){
		var jwkset = new JWKSet(rsaKey);
 
		return (jwkSelector , context) -> jwkSelector.select(jwkset);
	}

	@Bean
	JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
		return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
	} 

	
	@Bean
	JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}
 

}
