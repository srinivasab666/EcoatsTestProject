package com.ecoat.management.ecoatapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_employee_goals")
@Builder
public class EmployeeGoals implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_goal_id")
    private long employeeGoalId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "goal_category")
    private String goalCategory;

    @Column(name = "goal_description")
    private String goalDescription;

    @Column(name = "goal_weightage")
    private Integer goalWeightage;

    @Column(name = "appraizee_comments")
    private String appraizeeComments;

    @Column(name = "appraiser_rating")
    private Integer appraiserRating;

    @Column(name = "expiry_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date expiryDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_on")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdOn;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_on")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedOn;

    @Column(name = "is_active")
    private int isActive;
}
