package com.example.demo.springconfiguration;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Value("${spring.application.name}")
	private String applicationName;

	@Bean
	OpenAPI apiInfo() {
		return new OpenAPI().info(new Info().title("Employee Performance Tracker").description("This is the detailed API documentation of the Employee Performance Tracker").version("v1.0").summary("This documentation emphasized on the detailed working of different End Points"));
	}

	@Bean
	GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder().group("v1").packagesToScan("com.example.demo") // change to your package
				.build();
	}

}
