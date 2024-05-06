package com.ecoat.management.ecoatapi.mapper;

import com.ecoat.management.ecoatapi.dto.CorporateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ecoat.management.ecoatapi.dto.RegistrationDTO;
import com.ecoat.management.ecoatapi.model.Corporate;
import com.ecoat.management.ecoatapi.model.CorporateAddress;

@Mapper(componentModel = "spring")
public interface CorporateMapper {

	@Mapping(target = "corporateCode", source = "regDto.orgCode")
    @Mapping(target = "corporateName", source = "regDto.orgName")
    @Mapping(target = "createdBy", source = "regDto.userEmail")
	@Mapping(target = "isActive", constant = "1")
	@Mapping(target = "createdOn", expression = "java(new java.util.Date())")
	public abstract Corporate mapRegDtoToCorporate(RegistrationDTO regDto);
	
	@Mapping(target = "corporate", source = "corporate")
    @Mapping(target = "address1", source = "regDto.orgAddr1")
    @Mapping(target = "address2", source = "regDto.orgAddr2")
    @Mapping(target = "city", source = "regDto.orgCity")
	@Mapping(target = "state", source = "regDto.orgState")
	@Mapping(target = "zip", source = "regDto.orgZip")
	@Mapping(target = "country", source = "regDto.orgCountry")
	@Mapping(target = "phone", source = "regDto.orgPhone")
	@Mapping(target = "fax", source = "regDto.orgFax")
	@Mapping(target = "website", source = "regDto.webSite")
	@Mapping(target = "createdBy", source = "regDto.userEmail")
	@Mapping(target = "createdOn", expression = "java(new java.util.Date())")
	@Mapping(target = "isActive", constant = "1")
	public abstract CorporateAddress mapRegDtoToCorpAddress(Corporate corporate,RegistrationDTO regDto);

	@Mapping(target = "corporateCode", source = "corpDTO.orgCode")
	@Mapping(target = "corporateName", source = "corpDTO.orgName")
	@Mapping(target = "isActive", constant = "1")
	public abstract Corporate mapCorpDTOToCorporate(CorporateDTO corpDTO);

	@Mapping(target = "corporate", source = "corporate")
	@Mapping(target = "address1", source = "corpDTO.orgAddr1")
	@Mapping(target = "address2", source = "corpDTO.orgAddr2")
	@Mapping(target = "city", source = "corpDTO.orgCity")
	@Mapping(target = "state", source = "corpDTO.orgState")
	@Mapping(target = "zip", source = "corpDTO.orgZip")
	@Mapping(target = "country", source = "corpDTO.orgCountry")
	@Mapping(target = "phone", source = "corpDTO.orgPhone")
	@Mapping(target = "fax", source = "corpDTO.orgFax")
	@Mapping(target = "website", source = "corpDTO.webSite")
	@Mapping(target = "isActive", constant = "1")
	public abstract CorporateAddress mapCorpDTOToCorpAddress(Corporate corporate,CorporateDTO corpDTO);
}
