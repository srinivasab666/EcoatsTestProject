package com.ecoat.management.ecoatapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecoat.management.ecoatapi.dto.CorporateDTO;
import com.ecoat.management.ecoatapi.dto.UpdateCorporateDTO;
import com.ecoat.management.ecoatapi.exception.CorporateAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.model.Corporate;
import com.ecoat.management.ecoatapi.model.CorporateAddress;

@Service
public interface CorporateService {
    Corporate addCorporate(CorporateDTO corpDTO) throws CorporateAlreadyExistsException;
    Corporate updateCorporate(UpdateCorporateDTO corpDTO, MultipartFile orgLogo) throws CorporateNotFoundException;
    Corporate getCorporate(String corpName) throws CorporateNotFoundException;
    Corporate getCorporate(Long id) throws CorporateNotFoundException;
	Boolean deleteCorpAddress(Long caId);
}
