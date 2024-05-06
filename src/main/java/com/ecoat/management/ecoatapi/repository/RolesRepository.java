package com.ecoat.management.ecoatapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecoat.management.ecoatapi.enums.RolesEnum;
import com.ecoat.management.ecoatapi.model.Role;

public interface RolesRepository extends JpaRepository<Role, Long>  {

	@Query("Select r from Role r where r.roleEnum = :roleName")
	Optional<Role> findByRoleName(@Param("roleName") RolesEnum roleName);

}
