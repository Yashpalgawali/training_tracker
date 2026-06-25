package com.example.demo.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class CacheConfig {

	@Bean
	CacheManager cacheManager() {
	
		CaffeineCacheManager manager = new CaffeineCacheManager("employees");
		
		manager.setCaffeine(
				Caffeine.newBuilder()
						.initialCapacity(100)
						.maximumSize(1000)
						.expireAfterAccess(10, TimeUnit.MINUTES)				
				);
		
		return manager;
	}
}
