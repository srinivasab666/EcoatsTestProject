package com.ecoat.management.ecoatapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClientAddressReqDTO extends ClientAddressDTO{

	private long clientAddressId;
}
