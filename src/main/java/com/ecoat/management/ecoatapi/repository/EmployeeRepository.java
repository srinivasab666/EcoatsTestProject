package com.ecoat.management.ecoatapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecoat.management.ecoatapi.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>  {

    Optional<Employee> findByEmail(String emailAddr);

    @Query(value="select emp from Employee emp where emp.dept.deptId=?1")
    Optional<List<Employee>> findEmployeesByDeptId(long deptId);

    @Query(value="select emp from Employee emp where emp.corporate.id=?1")
    Optional<List<Employee>> findEmployeesByCorpId(long corpId);
    
    
    @Query(value="select e1.* from tbl_employee e1 join tbl_employee e2 on e1.employee_id = e2.supervisor_id and e2.employee_id= :empId",nativeQuery = true)
    Optional<Employee> findManagerByEmpId(long empId);

    @Query(value="select emp from Employee emp where emp.manager.empId=:supId")
    List<Employee> findAllBySupervisorId(long supId);
    
    @Query(value="select emp from Employee emp where emp.empId = ?1 and emp.corporate.id=?2")
    Optional<Employee> findByEmpIdAndCorpId(long empId,long corpId);
}
