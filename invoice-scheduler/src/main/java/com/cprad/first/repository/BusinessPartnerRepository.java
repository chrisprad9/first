package com.cprad.first.repository;

import com.cprad.first.entity.BusinessPartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessPartnerRepository extends JpaRepository<BusinessPartnerEntity, Long> {
}
