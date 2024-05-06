package com.ecoat.management.ecoatapi.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_client")
public class Client implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "client_id")
	private long clientId;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "corporate_id")
	@JsonBackReference
	private Corporate corporate;
	@OneToMany(fetch = FetchType.LAZY, mappedBy="client", cascade = CascadeType.ALL)
	@JsonIgnoreProperties("client")
	private Set<ClientAddress> clientAddress;
	@Column(name = "client_name")
	private String clientName;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "created_on")
	private Date createdOn;
	@Column(name = "updated_by")
	private String updatedBy;
	@Column(name = "updated_on")
	private Date updatedOn;
	@Column(name = "is_active")
	private Integer isActive;

}
