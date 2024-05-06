package com.ecoat.management.ecoatapi.mapper;

import com.ecoat.management.ecoatapi.dto.ChargeCodeDTO;
import com.ecoat.management.ecoatapi.model.ChargeCode;
import com.ecoat.management.ecoatapi.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChargeCodeMapper {
    @Mapping(target = "project", source = "project")
    @Mapping(target = "chargeCodeName", source = "chargeCodeDTO.chargeCodeName")
    @Mapping(target = "effectiveDate", source = "chargeCodeDTO.effectiveDate")
    @Mapping(target = "expirationDate", source = "chargeCodeDTO.expirationDate")
    @Mapping(target = "corporateId", source = "chargeCodeDTO.corporateId")
    @Mapping(target = "isActive", constant = "1")
    @Mapping(target = "createdBy", source = "chargeCodeDTO.createdByEmail")
    @Mapping(target = "createdOn", expression = "java(new java.util.Date())")
    public abstract ChargeCode mapDTOToChargeCode(Project project, ChargeCodeDTO chargeCodeDTO);
}
