package com.ecoat.management.ecoatapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecoat.management.ecoatapi.dto.ClientDTO;
import com.ecoat.management.ecoatapi.dto.UpdateClientReqDTO;
import com.ecoat.management.ecoatapi.exception.ClientAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.ClientNotFoundException;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.model.Client;
import com.ecoat.management.ecoatapi.service.ClientService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/client")
@Slf4j
public class ClientController {

	private final ClientService clientService;
	
	@PostMapping("/addClient")
	public ResponseEntity<String> addClient(@Valid @RequestBody ClientDTO clientDTO) {
		String method = "ClientController.addClient ";
		log.info(method + "Enter");
		log.info("clientDTO : " + clientDTO);
		ResponseEntity<String> response = null;
		Client client = null;
		try {
			client = clientService.addClient(clientDTO);
			response = new ResponseEntity<>("Client is added successfully", HttpStatus.OK);
		} catch (ClientAlreadyExistsException | CorporateNotFoundException e) {
			response =  new ResponseEntity<>("Client is failed to add", HttpStatus.CONFLICT);
		} catch (Exception e) {
			response =  new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		log.info(method + "Exit");
		return response;
	}
	
	@GetMapping("/getClientByName")
	public ResponseEntity<Client> getClient(@RequestParam Long clientId){
		String method = "ClientController.getClient ";
		log.info(method + "Enter");
		log.info("clientId : " + clientId);
		ResponseEntity<Client> response= null;
		Client client = null;
		if (clientId <= 0) {
			throw new EcoatsException("The client Id should be greater than zero");
		}
		try {
			client = clientService.getClient(clientId);
			response = new ResponseEntity<>(client, HttpStatus.OK);
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage(), e);
		}
		log.info(method + "Exit");
		return response;
	}
	
	@GetMapping("/getClients")
	public ResponseEntity<List<Client>> getClients(){
		String method = "ClientController.getClients ";
		log.info(method + "Enter");
		List<Client> allClients = null;
		ResponseEntity<List<Client>> response= null;
		try {
			allClients = clientService.getClients();
			response = new ResponseEntity<List<Client>>(allClients, HttpStatus.OK);
			log.info(method + "Exit");
		}catch (Exception e) {
			throw new EcoatsException(e.getMessage(), e);
		}
		return response;
	}

	@GetMapping("/getClientsByCorp/{corpId}")
	public ResponseEntity<List<Client>> getClientsByCorp(@PathVariable("corpId") Long corpId){
		String method = "ClientController.getClientsByCorp ";
		log.info(method + "Enter");
		List<Client> allClientsByCorp = null;
		ResponseEntity<List<Client>> response= null;
		if (corpId <= 0) {
			throw new EcoatsException("The Corporation Id should be greater than zero");
		}
		try {
			allClientsByCorp = clientService.getClientsByCorp(corpId);
			response = new ResponseEntity<List<Client>>(allClientsByCorp, HttpStatus.OK);
			log.info(method + "Exit");
		}catch (Exception e) {
			throw new EcoatsException(e.getMessage(), e);
		}
		return response;
	}
	
	@PutMapping("/editClient")
	public ResponseEntity<Client> updateClient(@Valid @RequestBody UpdateClientReqDTO clientDTO){
		String method = "ClientController.updateClient ";
		log.info(method + "Enter");
		log.info("clientDTO : " + clientDTO);
		Client client = null;
		ResponseEntity<Client> response= null;
		try {
			client = clientService.editClient(clientDTO);
			response = new ResponseEntity<>(client, HttpStatus.OK);
			log.info(method + "Exit");
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage(), e);
		}
		return response;
	}
	
	@DeleteMapping("/deleteClient")
	public ResponseEntity<String> deleteClient(@RequestParam Long clientId){
		String method = "ClientController.deleteClient ";
		log.info(method + "Enter");
		ResponseEntity<String> response= null;
		if (clientId <= 0) {
			throw new EcoatsException("The client Id should be greater than zero");
		}
		try {
			clientService.deleteClient(clientId);
			response = new ResponseEntity<>("Client deleted successfully", HttpStatus.OK);
			log.info(method + "Exit");
		} catch (ClientNotFoundException e) {
			response =  new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
		response =  new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
		return response;
	}
}
