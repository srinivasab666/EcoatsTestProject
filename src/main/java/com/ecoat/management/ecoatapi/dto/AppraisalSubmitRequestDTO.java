package com.ecoat.management.ecoatapi.dto;

import java.util.List;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppraisalSubmitRequestDTO {

	private Long empId;
	private List<SubmitCategoryDTO> categoryList;
	private Long submitBy;
	private boolean isAppraiser;
	private Long startYear;
	private Long endYear;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SubmitCategoryDTO{
		@Min(value = 1, message = "categoryId can't be less than 1")
		private Long categoryId;
		private Float selfRating;
		private Float managerRating;
		
	}
}
