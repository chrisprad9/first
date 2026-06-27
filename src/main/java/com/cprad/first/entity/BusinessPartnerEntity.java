package com.cprad.first.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "business_partner")
public class BusinessPartnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 30)
    private String status = "ACTIVE";

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
