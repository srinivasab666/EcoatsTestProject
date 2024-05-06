package com.ecoat.management.ecoatapi.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

	private long projectId;
	@NotEmpty(message="clientName cannot be null")
	private String projectName;
	@NotEmpty(message="projectDescription cannot be null")
	private String projectDescription;
	@Min(value = 1,message = "clientId can't be less than 1")
	private long clientId;
	@Min(value = 1,message = "corporateId can't be less than 1")
	private long corporateId;
	private String userId; 
}
