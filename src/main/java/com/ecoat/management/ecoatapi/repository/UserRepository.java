package com.ecoat.management.ecoatapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecoat.management.ecoatapi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	//Optional<User> findByUserFirstName(String userFirstName);
	Optional<User> findByUserId(String userId);
	
	@Query(value = "SELECT * FROM tbl_user WHERE employee_id = ?1", nativeQuery = true)
	User findByEmployeeId(long l);
	
	 @Query(value = "select u.* from tbl_employee e inner join tbl_user u on e.employee_id = u.employee_id and e.email_address= :email and u.is_active = 1;", nativeQuery = true)
	 Optional<User> findActiveUserByEmail(String email);
}
