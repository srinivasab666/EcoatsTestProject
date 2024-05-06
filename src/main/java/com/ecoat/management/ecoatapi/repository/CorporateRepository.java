package com.ecoat.management.ecoatapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecoat.management.ecoatapi.model.Corporate;

@Repository
public interface CorporateRepository extends JpaRepository<Corporate, Long> {
	
	@Query(value = "SELECT * FROM tbl_corporate where corporate_name = :name and is_active = 1",nativeQuery = true)
    Optional<Corporate> findByCorporateName(String name);
}
