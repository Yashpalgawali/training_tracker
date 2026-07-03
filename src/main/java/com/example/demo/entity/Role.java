package com.example.demo.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Role implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 8829291010623623451L;

//	@Id
//	@SequenceGenerator(name = "role_seq",allocationSize = 1,initialValue = 1)
//    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "role_seq")
    private Long id;

    private String name; // e.g. "ROLE_USER", "ROLE_ADMIN"
}
