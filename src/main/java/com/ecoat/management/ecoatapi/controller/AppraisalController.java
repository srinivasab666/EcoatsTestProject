package com.ecoat.management.ecoatapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecoat.management.ecoatapi.dto.AppraisalCategoriesDTO;
import com.ecoat.management.ecoatapi.dto.AppraisalCommentsDTO;
import com.ecoat.management.ecoatapi.dto.AppraisalDTO;
import com.ecoat.management.ecoatapi.dto.AppraisalSubmitRequestDTO;
import com.ecoat.management.ecoatapi.dto.UpdateAppraisalCategoriesDTO;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.model.AppraisalCategories;
import com.ecoat.management.ecoatapi.model.AppraisalRating;
import com.ecoat.management.ecoatapi.model.response.AppraisalResponse;
import com.ecoat.management.ecoatapi.service.AppraisalService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/appraisal")
@Slf4j
public class AppraisalController {

	private final AppraisalService appraisalService;

	@PostMapping("/appraisalcomments")
	public ResponseEntity<List<String>> getAllAppraisalCommnets(
			@Valid @RequestBody AppraisalCommentsDTO appslCommntsDto) {
		String method = "AppraisalController.getAllAppraisalCommnets ";
		log.info(method + "Enter");
		log.info("empid : " + appslCommntsDto.getEmpID() + " From Date: " + appslCommntsDto.getFromDate() + " To Date: "
				+ appslCommntsDto.getToDate());
		ResponseEntity<List<String>> response = null;
		try {
			List<String> comments = appraisalService.getAllAppraisalComments(appslCommntsDto);
			response = new ResponseEntity<>(comments, HttpStatus.OK);
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage());
		}
		log.info(method + "Exit");
		return response;
	}

	@PostMapping("/addCategories")
	public ResponseEntity<AppraisalCategories> addAppraisalCategories(@Valid @RequestBody AppraisalCategoriesDTO appraisalCategoriesDTO) {
		String method = "AppraisalController.addAppraisalCategories ";
		log.info(method + "Enter");
		log.info(appraisalCategoriesDTO + "appraisalCategoriesDTO");
		ResponseEntity<AppraisalCategories> response = null;
		try {
			AppraisalCategories category = appraisalService.addAppraisalCategories(appraisalCategoriesDTO);
			response = new ResponseEntity<>(category, HttpStatus.OK);
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage());
		}
		log.info(method + "Exit");
		return response;
	}
	
	@PostMapping("/updateCategories")
	public ResponseEntity<AppraisalCategories> updateAppraisalCategories(@Valid @RequestBody UpdateAppraisalCategoriesDTO updateAppraisalCategoriesDTO) {
		String method = "AppraisalController.updateAppraisalCategories ";
		log.info(method + "Enter");
		ResponseEntity<AppraisalCategories> response = null;
		try {
			AppraisalCategories category = appraisalService.updateAppraisalCategory(updateAppraisalCategoriesDTO);
			response = new ResponseEntity<>(category, HttpStatus.OK);
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage());
		}
		log.info(method + "Exit");
		return response;
	}
	
	@DeleteMapping("/category")
	public ResponseEntity<String> deleteAppraisalCategory(@RequestParam Long categoryId) {
		String method = "AppraisalController.deleteAppraisalCategory ";
		log.info(method + "Enter");
		ResponseEntity<String> response = null;
		if (categoryId <= 0) {
			throw new EcoatsException("Category id shouldn't be less than zero");
		}
		try {
			appraisalService.deleteCategory(categoryId);
			response = new ResponseEntity<>("Category is deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		log.info(method + "Exit");
		return response;
	}
	
	@DeleteMapping("/categoryCommentId")
	public ResponseEntity<String> deleteAppraisalCategoryCommnet(@RequestParam Long categoryId,@RequestParam Long categoryCommentId) {
		String method = "AppraisalController.deleteAppraisalCategoryCommnet ";
		log.info(method + "Enter");
		ResponseEntity<String> response = null;
		if (categoryId <= 0 || categoryCommentId <= 0) {
			throw new EcoatsException("Category or Category comment id shouldn't be less than zero");
		}
		try {
			appraisalService.deleteCategoryComment(categoryId, categoryCommentId);
			response = new ResponseEntity<>("Category Comment is deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		log.info(method + "Exit");
		return response;
	}
	
	@GetMapping("/categories")
	public ResponseEntity<List<AppraisalCategories>> getAppraisalCategories(@RequestParam Long corpId) {
		String method = "AppraisalController.getAppraisalCategories ";
		log.info(method + "Enter");
		ResponseEntity<List<AppraisalCategories>> response = null;
		if (corpId <= 0) {
			throw new EcoatsException("corporation id shouldn't be less than zero");
		}
		try {
		List<AppraisalCategories> categoriesList = appraisalService.getAppraisalCategories(corpId);
		response = new ResponseEntity<>(categoriesList, HttpStatus.OK);
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage());
		}
		log.info(method + "Exit");
		return response;
	}
	
	@PostMapping("/addAppraisal")
	public ResponseEntity<String> addEmployeeAppraisal(@Valid @RequestBody AppraisalDTO appriaserDto) {
		String method = "AppraisalController.addEmployeeAppraisal ";
		log.info(method + "Enter");
		ResponseEntity<String> response = null;
		try {
			appraisalService.addAppraisal(appriaserDto);
			response = new ResponseEntity<>("appraisal added successfully", HttpStatus.OK);
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage());
		}
		log.info(method + "Exit");
		return response;
	}
	
	@PostMapping("/submitAppraisal")
	public ResponseEntity<List<AppraisalRating>> submitAppraisal(@Valid @RequestBody AppraisalSubmitRequestDTO appriaserReq) {
		String method = "AppraisalController.submitAppraisalByEmployee ";
		log.info(method + "Enter");
		ResponseEntity<List<AppraisalRating>> response = null;
		try {
			List<AppraisalRating> ratingsList = appraisalService.submitAppraisalByEmployee(appriaserReq);
			response = new ResponseEntity<>(ratingsList, HttpStatus.OK);
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage());
		}
		log.info(method + "Exit");
		return response;
	}
	
	@GetMapping("/appraisalByEmp")
	public ResponseEntity<AppraisalResponse> getAppraisalByEmployee(@RequestParam Long empId,@RequestParam Long startYear,@RequestParam Long endYear) {
		String method = "AppraisalController.getAppraisalByEmployee ";
		log.info(method + "Enter");
		ResponseEntity<AppraisalResponse> response = null;
		try {
			AppraisalResponse ar = appraisalService.getEmployeeAppraisal(empId,startYear,endYear);
			response = new ResponseEntity<>(ar, HttpStatus.OK);
		} catch (Exception e) {
			throw new EcoatsException(e.getMessage());
		}
		log.info(method + "Exit");
		return response;
	}
}
