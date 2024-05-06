package com.ecoat.management.ecoatapi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_corporate")
public class Corporate  implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6912758316465507816L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "corporate_id")
    private long id;
    @Column(name="corporate_code")
    @NotNull
    private String corporateCode;
    @Column(name="corporate_name")
    @NotNull
    private String corporateName;
    
    @Column(name="corporate_logo")
    private byte[] corporateLogo;
    
    @Column(name="is_active")
    private Integer isActive;
    @Column(name="created_by")
    private String createdBy;
    @Column(name="created_on",updatable = false)
    private Date createdOn;
    @Column(name="updated_by")
    private String updatedBy;
    @Column(name="updated_on",updatable = false)
    private Date updatedOn;

	@OneToMany(mappedBy = "corporate",cascade = CascadeType.ALL) 
	@JsonManagedReference
	private List<CorporateAddress> corpAddresses;
}
