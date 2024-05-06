package com.ecoat.management.ecoatapi.repository;

import com.ecoat.management.ecoatapi.model.EmployeeBilling;
import com.ecoat.management.ecoatapi.model.EmployeeGoals;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeBillingRepository extends JpaRepository<EmployeeBilling, Long>  {

    @Query(value="select eb from EmployeeBilling eb where eb.employee.empId=?1 and eb.billingType=?2 and eb.isActive=1")
    EmployeeBilling findByEmployeeIdAndBillingType(long empId,String billingType);

    @Query(value="select eb from EmployeeBilling eb where eb.employee.empId=?1")
    List<EmployeeBilling> findAllEmployeeBillingDetails(long empId);
}
