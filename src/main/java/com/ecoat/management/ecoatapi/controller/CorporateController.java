package com.ecoat.management.ecoatapi.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecoat.management.ecoatapi.dto.CorporateDTO;
import com.ecoat.management.ecoatapi.dto.UpdateCorporateDTO;
import com.ecoat.management.ecoatapi.dto.UpdateCorporateDTO.CorpAddress;
import com.ecoat.management.ecoatapi.exception.CorporateAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.model.Corporate;
import com.ecoat.management.ecoatapi.model.CorporateAddress;
import com.ecoat.management.ecoatapi.service.CorporateService;
import com.ecoat.management.ecoatapi.util.ValidationUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class CorporateController {
    private final CorporateService corporateService;

    @GetMapping("/corporate/{orgName}")
    public ResponseEntity<Corporate> getCorporateDetails(@PathVariable("orgName") String orgName) {
    	String method = "CorporateController.getCorporateDetails ";
		log.info(method + "Enter");
		log.info("orgName : " + orgName);
        try {
            Corporate corp = corporateService.getCorporate(orgName);
            log.info(method + "Exit");
            return new ResponseEntity<>(corp, HttpStatus.OK);
        } catch (CorporateNotFoundException corporateNotFoundException) {
            return new ResponseEntity<>(new Corporate(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/corporate/id/{corpId}")
    public ResponseEntity<Corporate> getCorporateDetails(@PathVariable("corpId") Long corpId) {
    	String method = "CorporateController.getCorporateDetails ";
		log.info(method + "Enter");
		log.info("corpId : " + corpId);
        try {
            Corporate corp = corporateService.getCorporate(corpId);
            log.info(method + "Exit");
            return new ResponseEntity<>(corp, HttpStatus.OK);
        } catch (CorporateNotFoundException ce) {
        	throw new EcoatsException(ce.getMessage());
        } catch(Exception e) {
        	throw new EcoatsException(e.getMessage());
        }
    }

    @PostMapping("/corporate")
    public ResponseEntity<Corporate> addCorporateDetails(@Valid @RequestBody CorporateDTO corporateDTO) {
    	String method = "CorporateController.addCorporateDetails ";
		log.info(method + "Enter");
		log.info("corporateDTO : " + corporateDTO);
        try {
        	Corporate corporate = corporateService.addCorporate(corporateDTO);
        	 log.info(method + "Exit");
            return new ResponseEntity<>(corporate, HttpStatus.CREATED);
        } catch (CorporateAlreadyExistsException ce) {
            return new ResponseEntity<>(new Corporate(), HttpStatus.CONFLICT);
        } catch(Exception e) {
        	throw new EcoatsException(e.getMessage());
        }
    }

    @PutMapping("/corporate")
    public ResponseEntity<Corporate> updateCorporateDetails(@Valid @RequestPart String corporateDTO,@RequestPart(name = "logo", required=false) MultipartFile orgLogo) throws JsonMappingException, JsonProcessingException {
        try {
        	String method = "CorporateController.updateCorporateDetails ";
    		log.info(method + "Enter");
    		log.info("corporateDTO : " + corporateDTO);
        	ObjectMapper mapper = new ObjectMapper();
        	UpdateCorporateDTO corpDTO;
        	boolean isValid = false;
        	corpDTO = mapper.readValue(corporateDTO, UpdateCorporateDTO.class);
        	for (CorpAddress ca : corpDTO.getCorpAddresses()) {
        		isValid = ValidationUtil.isValid(corpDTO.getCorporateName(),ca.getAddress1(),
        				ca.getCity(),ca.getState(),ca.getZip(),ca.getCountry(),ca.getPhone());
			}
        	if((null != corpDTO && corpDTO.getId()!=0) && isValid){
        		Corporate corporate = corporateService.updateCorporate(corpDTO,orgLogo);
        		log.info(method + "Exit");
        		return new ResponseEntity<>(corporate, HttpStatus.OK);
        	}else{
        		return new ResponseEntity<>(new Corporate(), HttpStatus.BAD_REQUEST);
        	}
        } catch (Exception e) {
        	log.error(e.getMessage());
            return new ResponseEntity<>(new Corporate(), HttpStatus.CONFLICT);
        }
    }

	@DeleteMapping("/corporate/corpAddressid/{caId}")
	public ResponseEntity<String> deleteCorpAddress(@PathVariable("caId") Long caId) {
		String method = "CorporateController.updateCorporateDetails ";
		log.info(method + "Enter");
		try {
			Boolean isDeleted = corporateService.deleteCorpAddress(caId);
			if (isDeleted) {
				log.info(method + "Exit");
				return new ResponseEntity<>("Successfully inactivated the corporate address", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Not able to delete the corporate address", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
}
