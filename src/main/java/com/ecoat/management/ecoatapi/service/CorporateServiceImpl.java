package com.ecoat.management.ecoatapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecoat.management.ecoatapi.dto.CorporateDTO;
import com.ecoat.management.ecoatapi.dto.UpdateCorporateDTO;
import com.ecoat.management.ecoatapi.dto.UpdateCorporateDTO.CorpAddress;
import com.ecoat.management.ecoatapi.exception.CorporateAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.mapper.CorporateMapper;
import com.ecoat.management.ecoatapi.model.Corporate;
import com.ecoat.management.ecoatapi.model.CorporateAddress;
import com.ecoat.management.ecoatapi.repository.CorporateAddressRepository;
import com.ecoat.management.ecoatapi.repository.CorporateRepository;
import com.ecoat.management.ecoatapi.util.CommonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CorporateServiceImpl implements CorporateService{

    private final CorporateRepository corpRepo;
    private final CorporateAddressRepository corpAddressRepo;
    private final CorporateMapper corporateMapper;

    @Override
    public Corporate getCorporate(String corpName) throws CorporateNotFoundException{
    	String method = "CorporateServiceImpl.getCorporate - corpName ";
		log.info(method + "Enter");
        Optional<Corporate> existingCorp = corpRepo.findByCorporateName(corpName);
        if(existingCorp.isPresent()){
            log.info("Corporate fetched successfully..."+corpName);
            existingCorp.get().getCorpAddresses().removeIf(ca -> ca.getIsActive()==0);
            log.info(method + "Exit");
            return existingCorp.get();
        }else{
            log.info("Corporate does not not exist..."+corpName);
            throw new CorporateNotFoundException();
        }
    }

    @Override
    public Corporate getCorporate(Long corpid) throws CorporateNotFoundException{
    	String method = "CorporateServiceImpl.getCorporate - corpId ";
		log.info(method + "Enter");
        Optional<Corporate> existingCorp = corpRepo.findById(corpid);
        if(existingCorp.isPresent()){
            log.info("Corporate fetched successfully..."+corpid);
            existingCorp.get().getCorpAddresses().removeIf(ca -> ca.getIsActive()==0);
            log.info(method + "Exit");
            return existingCorp.get();
        }else{
            log.info("Corporate does not not exist..."+corpid);
            throw new CorporateNotFoundException();
        }
    }

    @Override
    public Corporate addCorporate(CorporateDTO corpDTO) throws CorporateAlreadyExistsException {
    	String method = "CorporateServiceImpl.addCorporate ";
		log.info(method + "Enter");
        Optional<Corporate> existingCorp = corpRepo.findByCorporateName(corpDTO.getOrgName());
        if(!existingCorp.isPresent()){
        	List<CorporateAddress> addressList = new ArrayList<>();
            corpDTO.setOrgCode(CommonUtil.generateCode(corpDTO.getOrgName()));
            Corporate corporate = corporateMapper.mapCorpDTOToCorporate(corpDTO);
            corporate.setCreatedBy(corpDTO.getCreatedByEmail());
            corporate.setCreatedOn(new java.util.Date());
            CorporateAddress corpAddr = corporateMapper.mapCorpDTOToCorpAddress(corporate,corpDTO);
            corpAddr.setCreatedBy(corpDTO.getCreatedByEmail());
            corpAddr.setCreatedOn(new java.util.Date());
            addressList.add(corpAddr);
            corporate.setCorpAddresses(addressList);
            log.info(method + "Exit");
            return corpRepo.save(corporate);
        }else{
            log.info("Corporate already found..."+corpDTO.getOrgName());
            throw new CorporateAlreadyExistsException();
        }
    }

    @Override
    public Corporate updateCorporate(UpdateCorporateDTO corpDTO, MultipartFile orgLogo) throws CorporateNotFoundException{
    	String method = "CorporateServiceImpl.updateCorporate ";
		log.info(method + "Enter");
        Corporate corporate = null;
        Corporate savedCorp = null;
        Optional<Corporate> existingCorp = corpRepo.findById(corpDTO.getId());
        try {
        	 if(existingCorp.isPresent()){
                 corporate  = existingCorp.get();
                 if (orgLogo != null) {
                	 corporate.setCorporateLogo(orgLogo.getBytes());
                 }
                 corporate.setCorporateName(corpDTO.getCorporateName());
                 corporate.setCorporateCode(corpDTO.getCorporateCode());
                 corporate.setUpdatedBy(corpDTO.getUpdatedByEmail());
                 List<CorporateAddress> corpAddrsses = corporate.getCorpAddresses();
                 corpDTO.getCorpAddresses().stream().forEach((corpAddress)->{
                	 List<CorporateAddress> addList = corpAddrsses.stream().filter(c -> corpAddress.getCorpAddressid()==c.getCorpAddressid()).collect(Collectors.toList());
                	 CorporateAddress ca = null;
                	 if(addList != null && !addList.isEmpty()) {
                		  ca = addList.get(0);
                		 ca.setUpdatedBy(corpDTO.getUpdatedByEmail());
                	 }else {
                		 ca = new CorporateAddress();
                		 ca.setCreatedBy(corpDTO.getUpdatedByEmail());
                		 ca.setCorporate(existingCorp.get());
                		 ca.setIsActive(1);
                	 }
                	 ca.setAddress1(corpAddress.getAddress1());
            		 ca.setAddress2(corpAddress.getAddress2());
            		 ca.setCity(corpAddress.getCity());
            		 ca.setState(corpAddress.getState());
            		 ca.setZip(corpAddress.getZip());
            		 ca.setCountry(corpAddress.getCountry());
            		 ca.setPhone(corpAddress.getPhone());
            		 ca.setFax(corpAddress.getFax());
            		 ca.setPhone(corpAddress.getPhone());
            		 ca.setWebsite(corpAddress.getWebsite());
            		 corpAddrsses.add(ca);
                 });
                 savedCorp =  corpRepo.save(corporate);
                 log.info(method + "Exit");
             }else{
                 log.info("Corporate does not not exist..."+corpDTO.getCorporateName());
                 throw new CorporateNotFoundException();
             }
		}  catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
        return savedCorp;
    }

	@Override
	public Boolean deleteCorpAddress(Long caId) {
		String method = "CorporateServiceImpl.deleteCorpAddress ";
		log.info(method + "Enter");
		Boolean isSuccess = false;
		Optional<CorporateAddress>  caOpt = corpAddressRepo.findById(caId);
		if (caOpt.isPresent()) {
			CorporateAddress ca = caOpt.get();
			ca.setIsActive(0);
			corpAddressRepo.save(ca);
			 isSuccess = true;
			 log.info(method + "Exit");
		}else {
			throw new EcoatsException("Corporate Address is not found");
		}
		return isSuccess;
	}
}
