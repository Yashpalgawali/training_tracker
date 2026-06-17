package com.example.demo.dto;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ConfigurationProperties(prefix = "trainingtracker")
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class TrainingTrackerContactsInfoDto {

	private String message;
	
	private List<String> onCallSupport;
}
