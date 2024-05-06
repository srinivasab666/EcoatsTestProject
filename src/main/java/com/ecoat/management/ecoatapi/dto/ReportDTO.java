package com.ecoat.management.ecoatapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

	@Min(message = "employeeId must be greater than 0", value = 0L)
	private long employeeId;

	@NotNull(message = "fromDate cannot be null")
	private Date fromDate;

	@NotNull(message = "toDate cannot be null")
	private Date toDate;
}
