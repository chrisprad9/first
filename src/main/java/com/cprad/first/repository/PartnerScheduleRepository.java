package com.cprad.first.repository;

import com.cprad.first.entity.PartnerScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerScheduleRepository extends JpaRepository<PartnerScheduleEntity, Long> {
    // NATIVE QUERY AKAN DI TARUH DI SINI
}
