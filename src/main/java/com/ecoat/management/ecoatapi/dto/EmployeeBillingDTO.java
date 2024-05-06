package com.ecoat.management.ecoatapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBillingDTO {

	private long employeeBillingId;

	@Min(value = 1,message = "employeeId can't be less than 1")
	private Long employeeId;

	@Min(value = 0,message = "billingRate cannot be less than 0")
	private Double billingRate;

	@NotEmpty(message = "billingType cannot be empty")
	private String billingType;

	private String createdBy;

	private String updatedBy;
}
