package com.ecoat.management.ecoatapi.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecoat.management.ecoatapi.model.Client;
import com.ecoat.management.ecoatapi.model.ClientAddress;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

	@Query(value = "select c from Client c where c.clientName = ?1 and c.corporate.corporateName = ?2 ")
	Optional<Client> findByClientNameAndCorpName(String clientName,String corpName);

	@Query(value = "SELECT c FROM ClientAddress c WHERE c.client.clientId = ?1")
	Set<ClientAddress> findByClientID(long id);

	@Query(value = "SELECT c FROM ClientAddress c WHERE c.clientAddressId = ?1")
	ClientAddress findByClientAddressId(long clientAddressId);

	@Query(value = "select c from Client c where c.corporate.id = :id and c.isActive = 1 ")
	List<Client> findByCorporateId(long id);
	
	@Query(value = "select c from Client c where c.isActive = 1 ")
	List<Client> fetchActiveClients();

}
