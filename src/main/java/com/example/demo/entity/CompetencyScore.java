package com.example.demo.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor	
public class CompetencyScore implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6042746876800116781L;

	private String name;

	private Long score;
}
