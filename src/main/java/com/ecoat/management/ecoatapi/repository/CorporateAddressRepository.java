package com.ecoat.management.ecoatapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecoat.management.ecoatapi.model.CorporateAddress;

@Repository
public interface CorporateAddressRepository extends JpaRepository<CorporateAddress, Long> {
//    Optional<CorporateAddress> findByCorporateId(long id);
}
