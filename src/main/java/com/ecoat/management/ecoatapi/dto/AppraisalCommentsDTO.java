package com.ecoat.management.ecoatapi.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppraisalCommentsDTO implements Serializable {

	@NotNull(message = "empID cannot be null")
	private long empID;
	@NotNull(message = "fromDate cannot be null")
	@ApiModelProperty(example = "2022-12-24",value = "the from date for appraisal comments")
	private String fromDate;
	@NotNull(message = "toDate cannot be null")
	@JsonFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(example = "2022-12-24")
	private String toDate;
	
}
