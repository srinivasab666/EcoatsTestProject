package com.ecoat.management.ecoatapi.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_appraisal_categories_comments")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Builder
public class AppraisalCategoriesComment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8398055994990857102L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categories_id", nullable = false)
	@JsonBackReference
	private AppraisalCategories categories;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "created_on", nullable = false, updatable = false)
	@CreationTimestamp
	private Date createdOn;
	@Column(name = "updated_by")
	private String updatedBy;
	@Column(name = "updated_on", nullable = false, updatable = false)
	@UpdateTimestamp
	private Date updatedOn;
	@Column(name = "is_active")
	private Integer isActive;
}
