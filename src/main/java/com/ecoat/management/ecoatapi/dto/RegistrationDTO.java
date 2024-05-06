package com.ecoat.management.ecoatapi.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3617754814801947334L;
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
	@NotEmpty(message="firstName cannot be null")
	private String userFirstName;
	private String userMiddleName;
	@NotEmpty(message="lastName cannot be null")
	private String userLastName;
	private String userId;
	@NotEmpty(message="email cannot be null")
	private String userEmail;
	@NotEmpty(message="userPhone cannot be null")
	private String userPhone;
	@NotEmpty(message="password cannot be null")
	private String password;
	private Long supervisorId;
}
