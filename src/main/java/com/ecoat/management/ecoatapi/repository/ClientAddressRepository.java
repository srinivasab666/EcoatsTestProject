package com.ecoat.management.ecoatapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecoat.management.ecoatapi.model.ClientAddress;

@Repository
public interface ClientAddressRepository extends JpaRepository<ClientAddress, Long> {
}
