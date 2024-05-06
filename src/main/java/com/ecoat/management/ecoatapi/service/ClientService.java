package com.ecoat.management.ecoatapi.service;

import java.util.List;

import com.ecoat.management.ecoatapi.dto.ClientDTO;
import com.ecoat.management.ecoatapi.dto.UpdateClientReqDTO;
import com.ecoat.management.ecoatapi.exception.ClientAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.ClientNotFoundException;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.model.Client;

public interface ClientService {

	public Client addClient(ClientDTO clientDto) throws ClientAlreadyExistsException, CorporateNotFoundException;
	public Client getClient(Long clientId) throws ClientNotFoundException;
	public void deleteClient(Long clientId) throws ClientNotFoundException;
	public List<Client> getClients();
	List<Client> getClientsByCorp(Long corpId);
	Client editClient(UpdateClientReqDTO clientDto) throws CorporateNotFoundException, ClientNotFoundException;
}
