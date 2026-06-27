package com.cprad.first.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "partner_schedule")
public class PartnerScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String action;

    // Relasi Many-to-One ke BusinessPartner
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    private BusinessPartnerEntity partner;

    @Column(nullable = false, length = 20)
    private String frequency;

    private Integer day;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
