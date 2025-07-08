package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Table(name = "tbl_users")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Users {

	@Id
	@SequenceGenerator(name = "user_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "user_seq", strategy = GenerationType.AUTO)
	Long user_id;

	String username;

	String password;

	String email;

	int enabled;

	String role;

	@Transient
	String cnf_pass;

	@Transient
	String cnf_otp;

}
