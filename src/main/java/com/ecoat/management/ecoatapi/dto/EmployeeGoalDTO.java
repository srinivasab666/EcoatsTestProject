package com.ecoat.management.ecoatapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeGoalDTO {

	private long employeeGoalId;

	@Min(value = 1,message = "employeeId can't be less than 1")
	private Long employeeId;

	@NotEmpty(message = "goalCategory cannot be empty")
	private String goalCategory;

	@NotEmpty(message = "goalDescription cannot be empty")
	private String goalDescription;

	@Min(value = 0,message = "goalWeightage cannot be less than 0")
	@Max(value = 100,message = "goalWeightage cannot be greater than 100")
	private Integer goalWeightage;

	private String appraizeeComments;

	private Integer appraiserRating;

	private Date expiryDate;

	private String createdBy;

	private String updatedBy;
}
