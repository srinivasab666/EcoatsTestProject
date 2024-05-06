package com.ecoat.management.ecoatapi.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ecoat.management.ecoatapi.dto.AppraisalCategoriesDTO;
import com.ecoat.management.ecoatapi.dto.AppraisalCommentsDTO;
import com.ecoat.management.ecoatapi.dto.AppraisalDTO;
import com.ecoat.management.ecoatapi.dto.AppraisalSubmitRequestDTO;
import com.ecoat.management.ecoatapi.dto.UpdateAppraisalCategoriesDTO;
import com.ecoat.management.ecoatapi.exception.CorporateNotFoundException;
import com.ecoat.management.ecoatapi.exception.EcoatsException;
import com.ecoat.management.ecoatapi.model.AppraisalCategories;
import com.ecoat.management.ecoatapi.model.AppraisalCategoriesComment;
import com.ecoat.management.ecoatapi.model.AppraisalRating;
import com.ecoat.management.ecoatapi.model.Corporate;
import com.ecoat.management.ecoatapi.model.Employee;
import com.ecoat.management.ecoatapi.model.TimesheetEntry;
import com.ecoat.management.ecoatapi.model.response.AppraisalResponse;
import com.ecoat.management.ecoatapi.repository.AppraisalCategoriesCommentRepository;
import com.ecoat.management.ecoatapi.repository.AppraisalCategoryRepository;
import com.ecoat.management.ecoatapi.repository.AppraisalRepository;
import com.ecoat.management.ecoatapi.repository.CorporateRepository;
import com.ecoat.management.ecoatapi.repository.EmployeeRepository;
import com.ecoat.management.ecoatapi.repository.TimesheetEntryRepo;
import com.ecoat.management.ecoatapi.util.CommonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppraisalServiceImpl implements AppraisalService {

	private final TimesheetEntryRepo timesheetEntryRepo;
	private final EmployeeRepository employeeRepository;
	private final AppraisalRepository appraisalRepository;
	private final CorporateRepository corpRepository;
	private final AppraisalCategoryRepository appraisalCategoryRepo;
	private final AppraisalCategoriesCommentRepository appraisalCategoryCommentsRepo;
	
	private static final DecimalFormat df = new DecimalFormat("0.00");

	@Override
	public List<String> getAllAppraisalComments(AppraisalCommentsDTO appraisaDto) {
		String method = "AppraisalServiceImpl.getAllAppraisalComments ";
		log.info(method + "Enter");
		List<String> comments = new ArrayList<String>();
		List<TimesheetEntry> entrylist = getAllActiveTimesheetsByDate(appraisaDto);
		if (entrylist != null && !entrylist.isEmpty()) {
			comments = entrylist.stream().map(TimesheetEntry::getAppraisalComments).collect(Collectors.toList());
		}
		log.info(method + "Exit");
		return comments;
	}

	@Override
	public List<TimesheetEntry> getAllActiveTimesheetsByDate(AppraisalCommentsDTO appraisaDto) {
		String method = "AppraisalServiceImpl.getAllAppraisalComments ";
		log.info(method + "Enter");
		employeeRepository.findById(appraisaDto.getEmpID())
		.orElseThrow(() -> new EcoatsException("Specified Employee does not exist"));
		Date fromDate = CommonUtil.convertStringToDateMinMax(appraisaDto.getFromDate(), false);
		Date toDate = CommonUtil.convertStringToDateMinMax(appraisaDto.getToDate(), true);
		List<TimesheetEntry> entrylist = timesheetEntryRepo.getAllActiveTimesheetsByDate(appraisaDto.getEmpID(),
				fromDate, toDate);
		log.info(method + "Exit");
		return entrylist;
	}
	@Override
	public AppraisalCategories updateAppraisalCategory(UpdateAppraisalCategoriesDTO updateAppraisalCategoriesDTO) {
		AppraisalCategories updatedCategory = null;
		List<AppraisalCategoriesComment> commentsList = new ArrayList<>();
		Employee employee = employeeRepository.findById(updateAppraisalCategoriesDTO.getEmpId())
				.orElseThrow(() -> new EcoatsException("Appraisee is not found"));
		AppraisalCategories category = appraisalCategoryRepo.findById(updateAppraisalCategoriesDTO.getCategoryId()).orElseThrow(() -> new EcoatsException("category is not found"));
		if(!category.getName().equalsIgnoreCase(updateAppraisalCategoriesDTO.getCatogoryTitle())) {
			category.setName(updateAppraisalCategoriesDTO.getCatogoryTitle());
			category.setUpdatedBy(employee.getEmail());
		}
		
		updateAppraisalCategoriesDTO.getCatogoryCommentsDtlsList().forEach((cc) ->{
			AppraisalCategoriesComment  acc = appraisalCategoryRepo.findByCategoryCommentId(cc.getCommentId());
			if(acc != null) {
				acc.setName(cc.getCommentDesc());
				acc.setUpdatedBy(employee.getEmail());
				commentsList.add(acc);
			}else {
				AppraisalCategoriesComment catComment = AppraisalCategoriesComment.builder().createdBy(employee.getEmail())
						.categories(category).isActive(1).name(cc.getCommentDesc()).build();
				commentsList.add(catComment);
				log.info("Category comment Id "+ cc.getCommentId() + "is newly added.");
			}
		});
		category.getCategoriesComments().addAll(commentsList);
		updatedCategory = appraisalCategoryRepo.save(category);
		return updatedCategory;
	}
	
	@Override
	@Transactional
	public AppraisalCategories addAppraisalCategories(AppraisalCategoriesDTO appraisalCategoriesDTO) {
		String method = "AppraisalServiceImpl.getAppraisalCategories ";
		log.info(method + "Enter");
		Set<AppraisalCategoriesComment> appraisalCommntsList = new HashSet();
		Employee employee = employeeRepository.findById(appraisalCategoriesDTO.getEmpId())
				.orElseThrow(() -> new EcoatsException("Appraisee is not found"));
		Corporate corporate = corpRepository.findById(appraisalCategoriesDTO.getCorpId())
				.orElseThrow(() -> new CorporateNotFoundException());

		employeeRepository.findByEmpIdAndCorpId(appraisalCategoriesDTO.getEmpId(), appraisalCategoriesDTO.getCorpId())
				.orElseThrow(() -> new EcoatsException("Employee is not belonging to the corporation"));
		AppraisalCategories category = AppraisalCategories.builder().corporate(corporate).isActive(1)
				.name(appraisalCategoriesDTO.getCatogoryTitle()).createdBy(employee.getEmail()).build();
		appraisalCategoriesDTO.getCatogoryComments().stream().forEach((comment) -> {
			AppraisalCategoriesComment catComment = AppraisalCategoriesComment.builder().createdBy(employee.getEmail())
					.categories(category).isActive(1).name(comment).build();
			appraisalCommntsList.add(catComment);
		});
		category.setCategoriesComments(appraisalCommntsList);
		appraisalCategoryRepo.save(category);
		log.info(method + "Exit");
		return category;
	}
	
	@Override
	public void deleteCategory(Long categoryId) {
		String method = "AppraisalServiceImpl.deleteCategory ";
		log.info(method + "Enter");
		AppraisalCategories category = appraisalCategoryRepo.findById(categoryId).orElseThrow(() -> new EcoatsException("category is not found"));
		category.setIsActive(0);
		appraisalCategoryRepo.save(category);
		log.info(method + "Exit");
	}
	
	@Override
	@Transactional
	public void deleteCategoryComment(Long categoryId,Long commentId) {
		String method = "AppraisalServiceImpl.deleteCategoryComment ";
		log.info(method + "Enter");
		AppraisalCategories category = appraisalCategoryRepo.findByCategoryIdAndCommentId(categoryId,commentId);
		AppraisalCategoriesComment comment = appraisalCategoryRepo.findByCategoryCommentId(commentId);
		if(category != null && comment != null) {
			appraisalCategoryCommentsRepo.deleteById(commentId);
		}else {
			throw new EcoatsException("Category Comment is not found");
		}
		log.info(method + "Exit");
	}
	@Override
	public List<AppraisalCategories> getAppraisalCategories(Long corpId)  {
		String method = "AppraisalServiceImpl.getAppraisalCategories ";
		log.info(method + "Enter");
		corpRepository.findById(corpId).orElseThrow(() -> new CorporateNotFoundException());
		List<AppraisalCategories> acList = appraisalCategoryRepo.fetchAppraisalCategories(corpId);
		log.info(method + "Exit");
		return acList;
	}

	@Override
	@Transactional
	public void addAppraisal(AppraisalDTO appriasalDto) {
		String method = "AppraisalServiceImpl.addAppraisal ";
		log.info(method + "Enter");
		List<AppraisalRating> ratingsList = new ArrayList<>();
		Employee appraisee = employeeRepository.findById(appriasalDto.getAppraiseeId())
				.orElseThrow(() -> new EcoatsException("Appraisee is not found"));
		Employee appraiser = employeeRepository.findById(appriasalDto.getAppriaserId())
				.orElseThrow(() -> new EcoatsException("Appraiser is not found"));
		
		Optional<List<AppraisalRating>> arOpt1 = appraisalRepository.findAppraisalByEmpIdAndStartAndEndYears(appriasalDto.getAppraiseeId(),
				appriasalDto.getStartYear(),appriasalDto.getEndYear());
		
		if(arOpt1.isPresent() && !arOpt1.get().isEmpty()) {
			removeUnselectedAppraisal(arOpt1.get(),appriasalDto.getCategoryList());
		}
		
		appriasalDto.getCategoryList().stream().forEach((categoryId) -> {
			Optional<AppraisalRating> arOpt = appraisalRepository.findByEmployeeAndCategory(appraisee.getEmpId(),
					categoryId,appriasalDto.getStartYear(),appriasalDto.getEndYear());
			AppraisalCategories ac = appraisalCategoryRepo.findByCategory(categoryId);
			
			if (arOpt.isPresent()) {
//				throw new EcoatsException(
//						"appraisal category " + ac.getName() + " already added for " + appraisee.getEmail());
				log.info("appraisal category " + ac.getName() + " already added for " + appraisee.getEmail());
			} else {
				AppraisalRating ar = AppraisalRating.builder()// .appraiseeRating(c.getSelfRating()).appraiserRating(c.getManagerRating())
						.employee(appraisee)
						.startYear(appriasalDto.getStartYear())
						.endYear(appriasalDto.getEndYear())
						.appraiseeRating(0f)
						.appraiserRating(0f)
						.categories(ac).createdBy(appraiser.getEmail()).isActive(1).build();
				ratingsList.add(ar);
			}

		});
		appraisalRepository.saveAll(ratingsList);
		log.info(method + "Exit");
	}

	
	private void removeUnselectedAppraisal(List<AppraisalRating> ratingList,List<Long> catList) {
		String method = "AppraisalServiceImpl.removeUnselectedAppraisal ";
		log.info(method + "Enter");
		List<AppraisalRating> filteredRatings = ratingList.stream().filter(r -> !catList.contains(r.getCategories().getId())).collect(Collectors.toList());
		List<Long> ids = filteredRatings.stream().map(AppraisalRating::getId).collect(Collectors.toList());
		appraisalRepository.deleteAllById(ids);
		log.info(method + "Exit");
	}
	
	@Override
	public List<AppraisalRating> submitAppraisalByEmployee(AppraisalSubmitRequestDTO appriaserReq) {
		String method = "AppraisalServiceImpl.submitAppraisalByEmployee ";
		log.info(method + "Enter");
		List<AppraisalRating> ratingsList = new ArrayList<>();
		Employee appraisee = employeeRepository.findById(appriaserReq.getEmpId()).orElseThrow(() -> new EcoatsException("Appraisee is not found"));
		
		appriaserReq.getCategoryList().stream().forEach((c) -> {
			AppraisalCategories ac = appraisalCategoryRepo.findByCategory(c.getCategoryId());
			AppraisalRating arOpt = appraisalRepository.findByEmployeeAndCategory(appraisee.getEmpId(),c.getCategoryId(),appriaserReq.getStartYear(),appriaserReq.getEndYear())
					.orElseThrow(() -> new EcoatsException("appraisal category " + ac.getName() + " is not found for " + appraisee.getEmail()));
			if (appriaserReq.isAppraiser()) {
				arOpt.setAppraiserRating(c.getManagerRating());
			} else {
				arOpt.setAppraiseeRating(c.getSelfRating());
			}
			ratingsList.add(arOpt);
		});
		appraisalRepository.saveAll(ratingsList);
		log.info(method + "Exit");
		return ratingsList;
	}

	@Override
	public AppraisalResponse getEmployeeAppraisal(Long empId, Long startYear, Long endYear) {
		String method = "AppraisalServiceImpl.getEmployeeAppraisal ";
		log.info(method + "Enter");
		AppraisalResponse appraisalResponse = new AppraisalResponse();
		Employee appraisee = employeeRepository.findById(empId).orElseThrow(() -> new EcoatsException("Appraisee is not found"));
		List<AppraisalRating> arOpt = appraisalRepository.findAppraisalByEmpIdAndStartAndEndYears(empId,startYear,endYear).orElseThrow(
				() -> new EcoatsException("Employee id: " + appraisee.getEmail() + " doesn't have any appraisal "));
		if (arOpt.size() > 0) {
			OptionalDouble empRatingAvg = arOpt.stream().mapToDouble(AppraisalRating::getAppraiseeRating).average();
			OptionalDouble mgrRatingAvg = arOpt.stream().mapToDouble(AppraisalRating::getAppraiserRating).average();
			appraisalResponse.setEmployeeFinalRating(new BigDecimal(empRatingAvg.getAsDouble()).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
			appraisalResponse.setManagerFinalRating(new BigDecimal(mgrRatingAvg.getAsDouble()).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
		}
		appraisalResponse.setAppraisalRating(arOpt);
		log.info(method + "Exit");
		return appraisalResponse;
	}

}
