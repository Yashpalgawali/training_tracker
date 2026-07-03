package com.example.demo.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name="tbl_activity")
@Entity @Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @Builder
public class Activity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5160518317596798551L;

	@Id
	@SequenceGenerator(name = "activity_seq",initialValue = 1,allocationSize = 1)
	@GeneratedValue(generator = "activity_seq",strategy = GenerationType.IDENTITY)
	private Integer activityId;
	
	private String activity;
	
	private String activityDate;
	
	private String activityTime;

	public Activity(String activity, String activityDate, String activityTime) {
		super();
		this.activity = activity;
		this.activityDate = activityDate;
		this.activityTime = activityTime;
	}
	
}
