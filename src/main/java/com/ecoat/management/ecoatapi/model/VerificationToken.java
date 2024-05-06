package com.ecoat.management.ecoatapi.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_verification_token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="token_id")
    private Long id;
    private String token;
    @OneToOne(cascade = CascadeType.ALL,fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name="expiration_date")
    private Date expDate;
}