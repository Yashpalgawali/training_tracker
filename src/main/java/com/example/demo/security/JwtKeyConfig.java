package com.example.demo.security;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nimbusds.jose.jwk.RSAKey;

@Configuration
public class JwtKeyConfig {

	@Value("${jwt.private-key-path}")
	private String privateKeyPath;

	@Value("${jwt.public-key-path}")
	private String publicKeyPath;

	@Bean
	RSAKey rsaKey() throws Exception {

		PrivateKey privateKey = loadPrivateKey(privateKeyPath);
		PublicKey publicKey = loadPublicKey(publicKeyPath);

		return new RSAKey.Builder((java.security.interfaces.RSAPublicKey) publicKey).privateKey(privateKey)
				.keyID("trainingtracker-key").build();
	}

	private PrivateKey loadPrivateKey(String path) throws Exception {

		String key = Files.readString(Path.of(path), StandardCharsets.UTF_8);

		key = key.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s+",
				"");

		byte[] decoded = Base64.getDecoder().decode(key);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		return keyFactory.generatePrivate(keySpec);
	}

	private PublicKey loadPublicKey(String path) throws Exception {

		String key = Files.readString(Path.of(path), StandardCharsets.UTF_8);

		key = key.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replaceAll("\\s+",
				"");

		byte[] decoded = Base64.getDecoder().decode(key);

		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		return keyFactory.generatePublic(keySpec);
	}
}
