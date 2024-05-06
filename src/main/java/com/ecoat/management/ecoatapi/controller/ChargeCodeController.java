package com.ecoat.management.ecoatapi.controller;

import com.ecoat.management.ecoatapi.dto.ChargeCodeDTO;
import com.ecoat.management.ecoatapi.dto.EmployeeDTO;
import com.ecoat.management.ecoatapi.exception.*;
import com.ecoat.management.ecoatapi.model.ChargeCode;
import com.ecoat.management.ecoatapi.model.Employee;
import com.ecoat.management.ecoatapi.service.ChargeCodeService;
import com.ecoat.management.ecoatapi.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ChargeCodeController {

    private final ChargeCodeService chargeCodeService;

    @GetMapping("/chargeCodes/id/{chargeCodeId}")
    public ResponseEntity<ChargeCode> getChargeCode(@PathVariable("chargeCodeId") String chargeCodeId) {
        try {
            return new ResponseEntity<>(chargeCodeService.getChargeCode(chargeCodeId), HttpStatus.OK);
        } catch (ChargeCodeNotFoundException exception) {
            return new ResponseEntity<>(new ChargeCode(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/chargeCodes/project/{projectId}")
    public ResponseEntity<List<ChargeCode>> getAllChargeCodeByProject(@PathVariable("projectId") long projectId) {
        try{
            return new ResponseEntity<>(chargeCodeService.getChargeCodesByProject(projectId),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/chargeCodes/corporate/{id}")
    public ResponseEntity<List<ChargeCode>> getAllChargeCodeByCorporate(@PathVariable("id") long id) {
        try{
            return new ResponseEntity<>(chargeCodeService.getChargeCodesByCorporate(id),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/chargeCodes")
    public ResponseEntity<List<ChargeCode>> getAllChargeCode() {
        return new ResponseEntity<>(chargeCodeService.getChargeCodes(),HttpStatus.OK);
    }

    @PostMapping("/chargeCodes")
    public ResponseEntity<ChargeCode> addChargeCode(@Valid @RequestBody ChargeCodeDTO chargeCodeDTO) {
        try {
            return new ResponseEntity<>(chargeCodeService.addChargeCode(chargeCodeDTO), HttpStatus.CREATED);
        } catch (ChargeCodeAlreadyExistsException exception) {
            return new ResponseEntity<>(new ChargeCode(), HttpStatus.CONFLICT);
        } catch (ProjectNotFoundException exception){
            return new ResponseEntity<>(new ChargeCode(), HttpStatus.CONFLICT);
        } catch (CorporateNotFoundException exception){
            return new ResponseEntity<>(new ChargeCode(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/chargeCodes")
    public ResponseEntity<ChargeCode> updateChargeCode(@Valid @RequestBody ChargeCodeDTO dto) {
        try {
            if(null!= dto.getChargeCdId() && !"".equals(dto.getChargeCdId())){
                return new ResponseEntity<>(chargeCodeService.updateChargeCode(dto), HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(new ChargeCode(), HttpStatus.BAD_REQUEST);
            }
        } catch (ChargeCodeNotFoundException exception) {
            return new ResponseEntity<>(new ChargeCode(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/chargeCodes/{chargeCodeId}/email/{emailAddress}")
    public ResponseEntity<String> deleteChargeCode(@PathVariable("chargeCodeId") String chargeCodeId, @PathVariable("emailAddress") String emailAddress) {
        try {
            chargeCodeService.deleteChargeCode(chargeCodeId,emailAddress);
            return new ResponseEntity<>("Employee delete successfully", HttpStatus.OK);
        } catch (ChargeCodeNotFoundException exception) {
            return new ResponseEntity<>("ChargeCode Not found", HttpStatus.CONFLICT);
        }
    }
}
