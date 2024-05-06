package com.ecoat.management.ecoatapi.service;

import com.ecoat.management.ecoatapi.dto.ChargeCodeDTO;
import com.ecoat.management.ecoatapi.exception.ChargeCodeAlreadyExistsException;
import com.ecoat.management.ecoatapi.exception.ChargeCodeNotFoundException;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.exception.ProjectNotFoundException;
import com.ecoat.management.ecoatapi.mapper.ChargeCodeMapper;
import com.ecoat.management.ecoatapi.model.ChargeCode;
import com.ecoat.management.ecoatapi.model.Corporate;
import com.ecoat.management.ecoatapi.model.Project;
import com.ecoat.management.ecoatapi.repository.ChargeCodeRepository;
import com.ecoat.management.ecoatapi.repository.CorporateRepository;
import com.ecoat.management.ecoatapi.repository.ProjectRepository;
import com.ecoat.management.ecoatapi.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ChargeCodeServiceImpl implements ChargeCodeService{
    private final ChargeCodeRepository chargeCodeRepository;
    private final ChargeCodeMapper chargeCodeMapper;
    private final ProjectRepository projectRepository;
    private final CorporateRepository corporateRepository;
    @Override
    public ChargeCode getChargeCode(String id) throws ChargeCodeNotFoundException {
        Optional<ChargeCode> existingChargeCode = chargeCodeRepository.findById(id);
        if(existingChargeCode.isPresent()){
            return existingChargeCode.get();
        }else{
            throw new ChargeCodeNotFoundException();
        }
    }

    @Override
    public List<ChargeCode> getChargeCodes() {
        return chargeCodeRepository.findAll();
    }

    @Override
    public List<ChargeCode> getChargeCodesByProject(long projectId) throws ChargeCodeNotFoundException{
        List<ChargeCode> chargeCodes = chargeCodeRepository.findByProjectId(projectId);
        if(null !=chargeCodes && !chargeCodes.isEmpty()){
            return chargeCodes;
        }else{
            throw new ChargeCodeNotFoundException();
        }
    }

    @Override
    public List<ChargeCode> getChargeCodesByCorporate(long id) throws ChargeCodeNotFoundException {
        List<ChargeCode> chargeCodes = chargeCodeRepository.findByCorpId(id);
        if(null !=chargeCodes && !chargeCodes.isEmpty()){
            return chargeCodes;
        }else{
            throw new ChargeCodeNotFoundException();
        }
    }

    @Override
    public ChargeCode addChargeCode(ChargeCodeDTO chargeCodeDTO) throws ChargeCodeAlreadyExistsException, ProjectNotFoundException, CorporateNotFoundException {
        Optional<ChargeCode> existingChargeCode = chargeCodeRepository.findByChargeCodeName(chargeCodeDTO.getChargeCodeName());
        if(existingChargeCode.isPresent()){
            throw new ChargeCodeAlreadyExistsException();
        }else {
            Optional<Project> existingProject = projectRepository.findById(chargeCodeDTO.getProjectId());
            Optional<Corporate> existingCorp = corporateRepository.findById(chargeCodeDTO.getCorporateId());
            if(existingProject.isPresent()){
                if(existingCorp.isPresent()){
                    ChargeCode newChargeCode = chargeCodeMapper.mapDTOToChargeCode(existingProject.get(),chargeCodeDTO);
                    newChargeCode.setChargeCodeId(CommonUtil.generateCode(chargeCodeDTO.getChargeCodeName()));
                    newChargeCode.setUpdatedBy(null);
                    newChargeCode.setUpdatedOn(null);
                    return chargeCodeRepository.save(newChargeCode);
                }else{
                    throw new CorporateNotFoundException();
                }
            }else{
                throw new ProjectNotFoundException();
            }
        }
    }

    @Override
    public ChargeCode updateChargeCode(ChargeCodeDTO chargeCodeDTO) throws ChargeCodeNotFoundException {
        Optional<ChargeCode> existingChargeCode = chargeCodeRepository.findById(chargeCodeDTO.getChargeCdId());
        if(existingChargeCode.isPresent()){
            ChargeCode chargeCode = chargeCodeMapper.mapDTOToChargeCode(existingChargeCode.get().getProject(),chargeCodeDTO);
            chargeCode.setIsActive(existingChargeCode.get().getIsActive());
            chargeCode.setChargeCodeId(existingChargeCode.get().getChargeCodeId());
            chargeCode.setCreatedBy(existingChargeCode.get().getCreatedBy());
            chargeCode.setCreatedOn(existingChargeCode.get().getCreatedOn());
            chargeCode.setUpdatedBy(chargeCodeDTO.getUpdatedByEmail());
            chargeCode.setUpdatedOn(new java.util.Date());
            return chargeCodeRepository.save(chargeCode);
        }else{
            throw new ChargeCodeNotFoundException();
        }
    }

    @Override
    public void deleteChargeCode(String id, String emailAddr) throws ChargeCodeNotFoundException {
        Optional<ChargeCode> existingChargeCode = chargeCodeRepository.findById(id);
        if(existingChargeCode.isPresent()){
            existingChargeCode.get().setIsActive(0);
            existingChargeCode.get().setUpdatedBy(emailAddr);
            existingChargeCode.get().setUpdatedOn(new java.util.Date());
            chargeCodeRepository.save(existingChargeCode.get());
        }else{
            throw new ChargeCodeNotFoundException();
        }
    }
}
