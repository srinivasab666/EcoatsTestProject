package com.ecoat.management.ecoatapi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_appraisal_categories")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Builder
public class AppraisalCategories implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5965058196374907103L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "corporate_id")
	@JsonBackReference
	private Corporate corporate;
//
	@OneToMany(mappedBy = "categories",cascade = CascadeType.ALL) 	
	private Set<AppraisalCategoriesComment> categoriesComments;

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
