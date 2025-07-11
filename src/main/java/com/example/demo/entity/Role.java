package com.example.demo.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

public class Role {
//	@Id
//	@SequenceGenerator(name = "role_seq",allocationSize = 1,initialValue = 1)
//    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "role_seq")
    private Long id;

    private String name; // e.g. "ROLE_USER", "ROLE_ADMIN"
}
