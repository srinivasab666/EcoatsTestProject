package com.ecoat.management.ecoatapi.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecoat.management.ecoatapi.dto.ClientDTO;
import com.ecoat.management.ecoatapi.dto.UpdateClientReqDTO;
import com.ecoat.management.ecoatapi.exception.ClientAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.ClientNotFoundException;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.mapper.ClientMapper;
import com.ecoat.management.ecoatapi.model.Client;
import com.ecoat.management.ecoatapi.model.ClientAddress;
import com.ecoat.management.ecoatapi.model.Corporate;
import com.ecoat.management.ecoatapi.repository.ClientRepository;
import com.ecoat.management.ecoatapi.repository.CorporateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepo;
	private final CorporateRepository corpRepo;
	private final ClientMapper clientMapper;

	@Override
	public Client addClient(ClientDTO clientDto) throws ClientAlreadyExistsException, CorporateNotFoundException {
		String method = "ClientServiceImpl.addClient ";
		log.info(method + "Enter");
		Client savedClient = null;
		Set<ClientAddress> clientAddressSet = new HashSet<ClientAddress>();
		Optional<Client> existingClient = clientRepo.findByClientNameAndCorpName(clientDto.getClientName(),clientDto.getOrgName());
		if (!existingClient.isPresent()) {
			Corporate corp = corpRepo.findByCorporateName(clientDto.getOrgName())
					.orElseThrow(() -> new CorporateNotFoundException());
			Client client = clientMapper.mapClientDTOToClient(clientDto, corp);
			client.setCreatedBy(clientDto.getUserId());
			client.setCreatedOn(new Date());
			clientDto.getClientAddresses().stream().forEach((ca) -> {
				ClientAddress clientAddress = clientMapper.mapClientAddressDTOToClientAddress(client,ca);
				clientAddress.setCreatedBy(clientDto.getUserId());
				clientAddress.setCreatedOn(new Date());
				clientAddressSet.add(clientAddress);
			});
			client.setClientAddress(clientAddressSet);
			savedClient = clientRepo.saveAndFlush(client);
			log.info("Client added successfully..." + clientDto.getClientName());
			log.info(method + "Exit");
		} else {
			log.info("Client already found..." + clientDto.getClientName());
			throw new ClientAlreadyExistsException();
		}
		return savedClient;
	}

	@Override
	public Client getClient(Long clientId) throws ClientNotFoundException {
		String method = "ClientServiceImpl.getClient ";
		log.info(method + "Enter");
		Optional<Client> existingClient = clientRepo.findById(clientId);
		if (existingClient.isPresent()) {
			log.info("Client fetched successfully..." + clientId);
			log.info(method + "Exit");
			return existingClient.get();
		} else {
			log.info("Client does not not exist..." + clientId);
			throw new ClientNotFoundException();
		}
	}

	@Override
	public Client editClient(UpdateClientReqDTO clientDto) throws CorporateNotFoundException, ClientNotFoundException {
		String method = "ClientServiceImpl.editClient ";
		log.info(method + "Enter");
		Client updatedClient = null;
		Set<ClientAddress> clientAddressSet = new HashSet<ClientAddress>();
		Optional<Client> existingClient = clientRepo.findById(clientDto.getClientId());
		if (existingClient.isPresent()) {
			Client client = existingClient.get();
			Corporate corporate = corpRepo.findByCorporateName(clientDto.getOrgName())
					.orElseThrow(() -> new CorporateNotFoundException());
			Client mappedClient = clientMapper.mapUpdatedClientDTOToClient(clientDto, corporate);
			mappedClient.setClientId(client.getClientId());
			mappedClient.setUpdatedBy(clientDto.getUserId());
			mappedClient.setUpdatedOn(new Date());
			clientDto.getClientAddresses().stream().forEach((ca) -> {
				ClientAddress existingAddress = clientRepo.findByClientAddressId(ca.getClientAddressId());
				ClientAddress clientAddress = clientMapper.mapClientAddressDTOToClientAddress(mappedClient,ca);
				if (existingAddress != null) {
					clientAddress.setClientAddressId(existingAddress.getClientAddressId());
					clientAddress.setUpdatedBy(clientDto.getUserId());
					clientAddress.setUpdatedOn(new Date());
				}else {
					clientAddress.setCreatedBy(clientDto.getUserId());
					clientAddress.setCreatedOn(new Date());
				}
				clientAddressSet.add(clientAddress);
			});
			mappedClient.setClientAddress(clientAddressSet);
			updatedClient = clientRepo.saveAndFlush(mappedClient);
			log.info(method + "Exit");
		} else {
			log.info("Client does not not exist..." + clientDto.getClientName());
			throw new ClientNotFoundException();
		}
		return updatedClient;
	}

	@Override
	public void deleteClient(Long clientId) {
		String method = "ClientServiceImpl.deleteClient ";
		log.info(method + "Enter");
		Optional<Client> existingClient = clientRepo.findById(clientId);
		if (existingClient.isPresent()) {
			Client client = existingClient.get();
			client.setIsActive(0);
			Set<ClientAddress> clientAddress = clientRepo.findByClientID(client.getClientId());
			Set<ClientAddress> updatedCaSet = clientAddress.stream().map(ca -> {
				ca.setClient(client);
				ca.setIsActive(0);
				return ca;
			}).collect(Collectors.toSet());
			client.setClientAddress(updatedCaSet);
			clientRepo.saveAndFlush(client);
			log.info(method + "Exit");
		} else {
			log.info("Client does not not exist...clientId ... " + clientId);
			throw new ClientNotFoundException();
		}
	}

	@Override
	public List<Client> getClients() {
		String method = "ClientServiceImpl.editClient ";
		log.info(method + "Enter");
		List<Client> clientsList = clientRepo.fetchActiveClients();
		log.info(method + "Exit");
		return clientsList;
	}

	@Override
	public List<Client> getClientsByCorp(Long corpId) {
		String method = "ClientServiceImpl.editClient ";
		log.info(method + "Enter");
		List<Client> clients = clientRepo.findByCorporateId(corpId);
		log.info(method + "Exit");
		return clients;
	}

}
