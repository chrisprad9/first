package com.cprad.first.entity;

import jakarta.persistence.*;

@Entity
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

    // --- GETTER & SETTER STANDARD ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public BusinessPartnerEntity getPartner() { return partner; }
    public void setPartner(BusinessPartnerEntity partner) { this.partner = partner; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public Integer getDay() { return day; }
    public void setDay(Integer day) { this.day = day; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
