package com.ecoat.management.ecoatapi.service;

import com.ecoat.management.ecoatapi.dto.ChargeCodeDTO;
import com.ecoat.management.ecoatapi.exception.ChargeCodeAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.ChargeCodeNotFoundException;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.exception.ProjectNotFoundException;
import com.ecoat.management.ecoatapi.model.ChargeCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChargeCodeService {
    ChargeCode getChargeCode(String id) throws ChargeCodeNotFoundException;
    List<ChargeCode> getChargeCodes();
    List<ChargeCode> getChargeCodesByProject(long id) throws ChargeCodeNotFoundException;
    List<ChargeCode> getChargeCodesByCorporate(long id) throws ChargeCodeNotFoundException;
    ChargeCode addChargeCode(ChargeCodeDTO chargeCodeDTO) throws ChargeCodeAlreadyExistsException, ProjectNotFoundException, CorporateNotFoundException;
    ChargeCode updateChargeCode(ChargeCodeDTO chargeCodeDTO) throws ChargeCodeNotFoundException;
    void deleteChargeCode(String id, String emailAddr) throws ChargeCodeNotFoundException;
}
