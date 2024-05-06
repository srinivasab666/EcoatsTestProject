package com.ecoat.management.ecoatapi.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.ecoat.management.ecoatapi.dto.AppraisalCategoriesDTO;
import com.ecoat.management.ecoatapi.dto.AppraisalCommentsDTO;
import com.ecoat.management.ecoatapi.dto.AppraisalDTO;
import com.ecoat.management.ecoatapi.dto.AppraisalSubmitRequestDTO;
import com.ecoat.management.ecoatapi.dto.UpdateAppraisalCategoriesDTO;
import com.ecoat.management.ecoatapi.model.AppraisalCategories;
import com.ecoat.management.ecoatapi.model.AppraisalRating;
import com.ecoat.management.ecoatapi.model.TimesheetEntry;
import com.ecoat.management.ecoatapi.model.response.AppraisalResponse;

@Service
public interface AppraisalService {
	List<String> getAllAppraisalComments(AppraisalCommentsDTO appraisaDto);

	List<AppraisalCategories> getAppraisalCategories(Long corpId);

	void addAppraisal(@Valid AppraisalDTO appriaserDto);

	List<AppraisalRating> submitAppraisalByEmployee(AppraisalSubmitRequestDTO appriaserReq);

	AppraisalResponse getEmployeeAppraisal(Long empId, Long startYear, Long endYear);

	AppraisalCategories addAppraisalCategories(AppraisalCategoriesDTO appraisalCategoriesDTO);

	AppraisalCategories updateAppraisalCategory(UpdateAppraisalCategoriesDTO updateAppraisalCategoriesDTO);

	List<TimesheetEntry> getAllActiveTimesheetsByDate(AppraisalCommentsDTO appraisaDto);

	void deleteCategory(Long categoryId);

	void deleteCategoryComment(Long categoryId, Long commentId);
}
