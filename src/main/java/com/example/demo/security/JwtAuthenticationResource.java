package com.example.demo.security;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtAuthenticationResource {

	private JwtEncoder jwtEncoder;
	
	public JwtAuthenticationResource(JwtEncoder jwtEncoder) {
		super();
		this.jwtEncoder = jwtEncoder;
	}

	@PostMapping("/authenticate")
	public JwtResponse postMethodName(Authentication authentication) {
		
		
		
		return new JwtResponse(createToken(authentication));
	}

	private String createToken(Authentication authentication) {
		
		JwtClaimsSet.builder()
						.issuer("self")
						.issuedAt(Instant.now())
						.expiresAt(Instant.now().plusSeconds(60 * 30))
						.subject(authentication.getName())
						.claim("scope", createScope(authentication))
						;
		
		return null;
	}

	private String createScope(Authentication authentication) {
		 
		String res = authentication.getAuthorities().stream().map(a->a.getAuthority()).collect(Collectors.joining(" "));
		return res;
	}
	
}

record JwtResponse(String token) {}
