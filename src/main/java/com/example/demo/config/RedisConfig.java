package com.example.demo.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

//    @Bean
//    RedisCacheConfiguration redisCacheConfiguration() {
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(30))
//                .disableCachingNullValues()
//                .serializeKeysWith(
//                        RedisSerializationContext.SerializationPair
//                                .fromSerializer(new StringRedisSerializer())
//                )
//                .serializeValuesWith(
//                        RedisSerializationContext.SerializationPair
//                                .fromSerializer(new GenericJackson2JsonRedisSerializer())
//                );
//    }
//
//    @Bean
//    RedisCacheManager cacheManager(
//            RedisConnectionFactory redisConnectionFactory,
//            RedisCacheConfiguration redisCacheConfiguration) {
//
//        RedisCacheConfiguration regulationTypeConfig =
//                redisCacheConfiguration.entryTtl(Duration.ofHours(2));
//
//        RedisCacheConfiguration employeeConfig =
//                redisCacheConfiguration.entryTtl(Duration.ofHours(15));
//
//        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
//        cacheConfigurations.put("regulationTypes", regulationTypeConfig);
//        cacheConfigurations.put("employees", employeeConfig);
//
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(redisCacheConfiguration)
//                .withInitialCacheConfigurations(cacheConfigurations)
//                .build();
//    }

	@Bean
	RedisCacheConfiguration redisCacheConfiguration() {

		return RedisCacheConfiguration.defaultCacheConfig()

				// Cache expiration
				.entryTtl(Duration.ofMinutes(30))

				// Do not cache null values
				.disableCachingNullValues()

				// Key serializer
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))

				// JSON value serializer
				.serializeValuesWith(RedisSerializationContext.SerializationPair
						.fromSerializer(new GenericJackson2JsonRedisSerializer()));
	}

	@Bean
	RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory,
			RedisCacheConfiguration redisCacheConfiguration) {

		RedisCacheConfiguration employeeConfig = redisCacheConfiguration.entryTtl(Duration.ofHours(2));

		RedisCacheConfiguration companyConfig = redisCacheConfiguration.entryTtl(Duration.ofMinutes(60));
		
		RedisCacheConfiguration committeeConfig = redisCacheConfiguration.entryTtl(Duration.ofMinutes(60));
		
		RedisCacheConfiguration categoriesConfig = redisCacheConfiguration.entryTtl(Duration.ofMinutes(60));

		Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

		cacheConfigurations.put("employees", employeeConfig);

		cacheConfigurations.put("companies", companyConfig);
		
		cacheConfigurations.put("committies", committeeConfig);
		
		cacheConfigurations.put("categories", categoriesConfig);
		
		return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(redisCacheConfiguration)
				.withInitialCacheConfigurations(cacheConfigurations).build();
	}
}
