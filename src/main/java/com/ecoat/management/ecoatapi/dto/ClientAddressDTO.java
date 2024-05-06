package com.ecoat.management.ecoatapi.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientAddressDTO {
    	@NotEmpty(message = "client Addr1 cannot be null")
    	private String clientAddress1;
    	private String clientAddress2;
    	@NotEmpty(message = "client city cannot be null")
    	private String city;
    	@NotEmpty(message = "client state cannot be null")
        private String state;
    	@NotEmpty(message = "client zip cannot be null")
        private String zip;
    	@NotEmpty(message = "client country cannot be null")
        private String country;
    	@NotEmpty(message = "client phone cannot be null")
        private String phone;
        private String fax;
        private String website;
    }