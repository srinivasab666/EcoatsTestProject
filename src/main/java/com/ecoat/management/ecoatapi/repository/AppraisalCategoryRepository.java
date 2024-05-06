package com.ecoat.management.ecoatapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecoat.management.ecoatapi.model.AppraisalCategories;
import com.ecoat.management.ecoatapi.model.AppraisalCategoriesComment;

public interface AppraisalCategoryRepository extends JpaRepository<AppraisalCategories,Long>  {
	
	@Query("select ac from AppraisalCategories ac where ac.corporate.id = :corpId and ac.isActive = 1")
	List<AppraisalCategories> fetchAppraisalCategories(Long corpId);
	
	@Query("select ac from AppraisalCategories ac where ac.id = :categoryId and ac.isActive = 1")
	AppraisalCategories findByCategory(Long categoryId);

	@Query(value = "SELECT cc FROM AppraisalCategoriesComment cc where cc.id = :commentId and cc.isActive = 1")
	AppraisalCategoriesComment findByCategoryCommentId(Long commentId);

	@Query(value ="select ac.* from tbl_appraisal_categories ac inner join tbl_appraisal_categories_comments acc on acc.categories_id = ac.id where ac.id = ?1 and acc.id= ?2 and ac.is_active = 1",nativeQuery = true)
	AppraisalCategories findByCategoryIdAndCommentId(Long categoryId, Long commentId);
}