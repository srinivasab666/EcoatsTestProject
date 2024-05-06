package com.ecoat.management.ecoatapi.dto;

import java.util.List;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppraisalDTO {

	@Min(value = 1, message = "appraiseeId can't be less than 1")
	private Long appraiseeId;
	private Long startYear;
	private Long endYear;
	@Min(value = 1, message = "appriaserId can't be less than 1")
	private Long appriaserId;
	private List<Long> categoryList;
	
}
