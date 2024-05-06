package com.ecoat.management.ecoatapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecoat.management.ecoatapi.model.ChargeCode;

public interface ChargeCodeRepository extends JpaRepository<ChargeCode,String> {
    Optional<ChargeCode> findByChargeCodeName(String chargeCodeName);

    @Query(value = "select * from tbl_charge_code where project_id=?1",nativeQuery = true)
    List<ChargeCode> findByProjectId(long projectId);

    @Query(value = "select * from tbl_charge_code where corporate_id=?1",nativeQuery = true)
    List<ChargeCode> findByCorpId(long id);
}