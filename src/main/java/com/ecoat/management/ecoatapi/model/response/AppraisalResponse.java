package com.ecoat.management.ecoatapi.model.response;

import java.util.List;

import com.ecoat.management.ecoatapi.model.AppraisalRating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppraisalResponse {

	private List<AppraisalRating> appraisalRating;
	private Double employeeFinalRating;
	private Double managerFinalRating;
	
	
}
