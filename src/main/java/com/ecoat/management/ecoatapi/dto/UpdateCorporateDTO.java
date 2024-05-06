package com.ecoat.management.ecoatapi.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCorporateDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5795176519995733587L;

	private long id;
	@NotEmpty(message = "orgName cannot be null")
	private String corporateName;
	private String corporateCode;
	private List<CorpAddress> corpAddresses;
	private String userId;
	private String updatedByEmail;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CorpAddress {
		private long corpAddressid;
		@NotEmpty(message = "orgAddr1 cannot be null")
		private String address1;
		private String address2;
		@NotEmpty(message = "org city cannot be null")
		private String city;
		@NotEmpty(message = "org state cannot be null")
		private String state;
		@NotEmpty(message = "org zip cannot be null")
		private String zip;
		@NotEmpty(message = "org country cannot be null")
		private String country;
		@NotEmpty(message = "org phone cannot be null")
		private String phone;
		private String fax;
		private String website;
	}
}
