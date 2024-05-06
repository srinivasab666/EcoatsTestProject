package com.ecoat.management.ecoatapi.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.ecoat.management.ecoatapi.dto.TimesheetDTO.TsEntryDtls;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppraisalCategoriesDTO {

	@Min(value = 1, message = "categoryId can't be less than 1")
	private long categoryId;
	@NotNull
	private String catogoryTitle;
	@NotNull
	private Long empId;
	private List<CatogoryCommentsDtls> catogoryCommentsDtlsList;
	
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CatogoryCommentsDtls {
		private Long commentId;
		private String commentDesc;
	}
}
