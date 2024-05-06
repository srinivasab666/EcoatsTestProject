package com.ecoat.management.ecoatapi.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_corprate_timesheet_setting")
@Builder
public class CorporateTimesheetSettings implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "corprate_timesheet_setting_id")
    private long corprateTimesheetSettingId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "corporate_id")
    private Corporate corporate;

    @Column(name = "timesheet_type")
    private String timesheetType;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_on")
    private Date updatedOn;

    @Column(name = "is_active")
    private int isActive;
}
