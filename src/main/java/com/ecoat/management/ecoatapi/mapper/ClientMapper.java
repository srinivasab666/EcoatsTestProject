package com.ecoat.management.ecoatapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ecoat.management.ecoatapi.dto.ClientAddressDTO;
import com.ecoat.management.ecoatapi.dto.ClientDTO;
import com.ecoat.management.ecoatapi.dto.UpdateClientReqDTO;
import com.ecoat.management.ecoatapi.model.Client;
import com.ecoat.management.ecoatapi.model.ClientAddress;
import com.ecoat.management.ecoatapi.model.Corporate;

@Mapper(componentModel = "spring")
public interface ClientMapper {

	@Mapping(target = "clientName", source = "clientDto.clientName")
	@Mapping(target = "corporate", source = "corporate")
	@Mapping(target = "isActive", constant = "1")
	Client mapClientDTOToClient(ClientDTO clientDto, Corporate corporate);

	@Mapping(target = "client", source = "client")
	@Mapping(target = "clientAddress1", source = "clientAddressDto.clientAddress1")
	@Mapping(target = "clientAddress2", source = "clientAddressDto.clientAddress2")
	@Mapping(target = "city", source = "clientAddressDto.city")
	@Mapping(target = "state", source = "clientAddressDto.state")
	@Mapping(target = "zip", source = "clientAddressDto.zip")
	@Mapping(target = "country", source = "clientAddressDto.country")
	@Mapping(target = "phone", source = "clientAddressDto.phone")
	@Mapping(target = "fax", source = "clientAddressDto.fax")
	@Mapping(target = "website", source = "clientAddressDto.website")
	@Mapping(target = "isActive", constant = "1")
	ClientAddress mapClientAddressDTOToClientAddress(Client client, ClientAddressDTO clientAddressDto);
	
	@Mapping(target = "clientAddress1", source = "clientAddressDto.clientAddress1")
	@Mapping(target = "clientAddress2", source = "clientAddressDto.clientAddress2")
	@Mapping(target = "city", source = "clientAddressDto.city")
	@Mapping(target = "state", source = "clientAddressDto.state")
	@Mapping(target = "zip", source = "clientAddressDto.zip")
	@Mapping(target = "country", source = "clientAddressDto.country")
	@Mapping(target = "phone", source = "clientAddressDto.phone")
	@Mapping(target = "fax", source = "clientAddressDto.fax")
	@Mapping(target = "website", source = "clientAddressDto.website")
	ClientAddress mapClientAddressDTOToClientAddress(ClientAddressDTO clientAddressDto);

	@Mapping(target = "clientName", source = "clientDto.clientName")
	@Mapping(target = "corporate", source = "corporate")
	@Mapping(target = "isActive", constant = "1")
	Client mapUpdatedClientDTOToClient(UpdateClientReqDTO clientDto, Corporate corporate);
	
}
