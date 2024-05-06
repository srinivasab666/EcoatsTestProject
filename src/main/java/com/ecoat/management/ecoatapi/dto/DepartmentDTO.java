package com.ecoat.management.ecoatapi.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {

	private long deptId;
	@NotEmpty(message="deptName cannot be null")
	private String deptName;
	@NotEmpty(message="deptLocation cannot be null")
	private String deptLocation;
    private long corpId;
    private String userId;
	
}
