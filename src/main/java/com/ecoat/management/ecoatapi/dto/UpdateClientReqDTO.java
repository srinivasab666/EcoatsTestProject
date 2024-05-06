package com.ecoat.management.ecoatapi.dto;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClientReqDTO {

	private long clientId;
	@NotEmpty(message="clientName cannot be null")
	private String clientName;
	@NotEmpty(message="orgName cannot be null")
	private String orgName;
    private String userId;
    private Set<UpdateClientAddressReqDTO> clientAddresses;
}
