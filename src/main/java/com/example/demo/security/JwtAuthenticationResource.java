package com.example.demo.security;

import java.time.Instant;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtAuthenticationResource {


	   private final JwtEncoder jwtEncoder;
	    private final AuthenticationManager authenticationManager;

	    public JwtAuthenticationResource(JwtEncoder jwtEncoder, AuthenticationManager authenticationManager) {
	        this.jwtEncoder = jwtEncoder;
	        this.authenticationManager = authenticationManager;
	    }
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	//This is for Angular
//	@PostMapping("/authenticate")
//	public 	JwtResponse authenticate(Authentication auth) {
//		return new JwtResponse(createToken(auth));
//	}
	
	 @PostMapping("/authenticate")
	    public JwtResponse authenticate(@RequestBody LoginRequest request) {
		 logger.info("Username is {} and password is {} ",request.username(),request.password());
	        // 1. Manually authenticate using AuthenticationManager
	        Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
	        // 2. Generate JWT
	        return new JwtResponse(createToken(authentication));
	      
	    }

	private String createToken(Authentication auth) {
		
		CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
		
		var claims = JwtClaimsSet.builder().issuer("self")
								.issuedAt(Instant.now())
								.expiresAt(Instant.now().plusSeconds(60*60))
								.subject(auth.getName()).claim("scope", createScope(auth))
								.claim("userId", user.getUserId()) // 👈 Custom claim: adds user_id to the payload of the token.
								.claim("username", user.getUsername()) // 👈 Custom claim: adds username to the payload of the token.
								.build() ;
								
		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}


	private String createScope(Authentication auth) {
		// TODO Auto-generated method stub
		String res = auth.getAuthorities().stream().map(a->a.getAuthority()).collect(Collectors.joining(" "));
		return res;
		
	}
}
record JwtResponse(String token) {}
record LoginRequest(String username, String password) {}
