package com.ecoat.management.ecoatapi.repository;

import com.ecoat.management.ecoatapi.model.Employee;
import com.ecoat.management.ecoatapi.model.EmployeeGoals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeGoalsRepository extends JpaRepository<EmployeeGoals, Long>  {

    @Query(value="select eg from EmployeeGoals eg where eg.employee.empId=?1 and eg.isActive=1")
    Optional<List<EmployeeGoals>> findEmployeeGoalsByEmployeeId(long empId);
}
