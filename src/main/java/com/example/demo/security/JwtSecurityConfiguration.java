package com.example.demo.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.Assert;
import org.springframework.web.cors.CorsConfiguration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class JwtSecurityConfiguration {

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
		
//		http.oauth2ResourceServer(  OAuth2ResourceServerConfigurer::jwt  );
		http.oauth2ResourceServer(oauth2 -> oauth2
			    .jwt(withDefaults())
			);
		return http.build();
	}
 
	
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
	

	
	
	@Bean
	KeyPair keyPair() throws NoSuchAlgorithmException {
		var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		return keyPairGenerator.generateKeyPair();
	}
	
	@Bean
	RSAKey rsaKey(KeyPair keyPair) {
		return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
					.privateKey(keyPair.getPrivate())
					.keyID(UUID.randomUUID().toString())
					.build(); 
	}
	
	 
	@Bean
	JWKSource<SecurityContext> jwkSource(RSAKey rsaKey)
	{
		var jwkset = new JWKSet(rsaKey);
		 
		return (jwkSelector,context ) -> jwkSelector.select(jwkset);
//		var jwkSource = new JWKSource<SecurityContext>() {
//
//			@Override
//			public List<JWK> get(JWKSelector jwkSelector, SecurityContext context) throws KeySourceException {
//				// TODO Auto-generated method stub
//				return jwkSelector.select(jwkset);
//			}
//		};
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
