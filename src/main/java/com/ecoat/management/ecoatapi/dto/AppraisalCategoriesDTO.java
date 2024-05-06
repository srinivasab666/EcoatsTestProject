package com.ecoat.management.ecoatapi.dto;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppraisalCategoriesDTO {
	
	
	@Min(value = 1, message = "corpId can't be less than 1")
	private long corpId;
	@NotNull
	private String catogoryTitle;
	private List<String> catogoryComments;
	private Long empId;
}
