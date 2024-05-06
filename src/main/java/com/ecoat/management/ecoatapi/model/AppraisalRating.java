package com.ecoat.management.ecoatapi.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_appraisal_employee_rating")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Builder
public class AppraisalRating implements Serializable   {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2701565126983119814L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categories_id")
	private AppraisalCategories categories;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
	@JsonBackReference
    private Employee employee;
	@Column(name = "start_year")
	private Long startYear;
	@Column(name = "end_year")
	private Long endYear;
	
	@Column(name = "self_rating")
    private Float appraiseeRating;
	@Column(name = "manager_rating")
    private Float appraiserRating;
	
	@Column(name = "created_by",nullable = false)
	private String createdBy;
	@Column(name = "created_on")
	@CreationTimestamp
	private Date createdOn;
	@Column(name = "updated_by")
	private String updatedBy;
	@Column(name = "updated_on",nullable = false)
	@UpdateTimestamp
	private Date updatedOn;
	@Column(name = "is_active")
	private Integer isActive;
}
