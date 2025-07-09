package com.example.demo.security;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
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
	public JwtResponse authenticate(Authentication authentication) {
		
		return new JwtResponse(createToken(authentication));
	}

	private String createToken(Authentication authentication) {
		
		var claims = JwtClaimsSet.builder().issuer("self")
				.issuedAt(Instant.now())
				.expiresAt(Instant.now().plusSeconds(60*30))
				.subject(authentication.getName()).claim("scope", createScope(authentication))
				.claim("scope", createScope(authentication)) // 👈 Custom claim: adds user_id to the payload of the token.
				   
				.build()
				
				;

		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		
	}

	private String createScope(Authentication authentication) {
		 
		String res = authentication.getAuthorities().stream().map(a->a.getAuthority()).collect(Collectors.joining(" "));
		return res;
	}
	
}

record JwtResponse(String token) {}
