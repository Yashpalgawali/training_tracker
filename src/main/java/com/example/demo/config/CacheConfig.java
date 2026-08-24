package com.example.demo.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import com.github.benmanes.caffeine.cache.Caffeine;

//@Configuration
public class CacheConfig {

//	@Bean
//	CacheManager cacheManager() {
//
////		CaffeineCacheManager cacheManager = new CaffeineCacheManager("employee", "employeeList");
////
////		cacheManager.setCaffeine(Caffeine.newBuilder().initialCapacity(10).maximumSize(5000)
////				.expireAfterWrite(30, TimeUnit.MINUTES).recordStats());
////
////		return cacheManager;
//		return null;
//	}

}
