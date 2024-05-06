package com.ecoat.management.ecoatapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorporateDTO implements Serializable {
	
    private static final long serialVersionUID = 3617754814801947334L;
    private long corpId;
    @NotEmpty(message="orgName cannot be null")
    private String orgName;
    private String orgCode;
    @NotEmpty(message="orgAddr1 cannot be null")
    private String orgAddr1;
    private String orgAddr2;
    @NotEmpty(message="org city cannot be null")
    private String orgCity;
    @NotEmpty(message="org state cannot be null")
    private String orgState;
    @NotEmpty(message="org zip cannot be null")
    private String orgZip;
    @NotEmpty(message="org country cannot be null")
    private String orgCountry;
    @NotEmpty(message="org phone cannot be null")
    private String orgPhone;
    private String orgFax;
    private String webSite;
    private String userId;
    private String createdByEmail;
    private String updatedByEmail;
}
