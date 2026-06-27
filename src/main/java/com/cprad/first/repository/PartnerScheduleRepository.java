package com.cprad.first.repository;

import com.cprad.first.entity.PartnerScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PartnerScheduleRepository extends JpaRepository<PartnerScheduleEntity, Long> {
    @Query(value = """
        SELECT ps.* FROM partner_schedule ps
        JOIN business_partner bp ON ps.partner_id = bp.id
        WHERE ps.is_active = true
          AND bp.status = 'ACTIVE'
          AND ps.action = :action
          AND (
            (ps.frequency = 'WEEKLY' AND ps.day = EXTRACT(ISODOW FROM :date))
        
            OR
        
            (ps.frequency = 'MONTHLY' AND (
                -- Normal Date (1 to 31)
                (ps.day > 0 AND ps.day = EXTRACT(DAY FROM :date))
                OR
                -- Back Date (0, -1, -2)
                (ps.day <= 0 AND ps.day = (
                    -- Real date subtracted by last date of that month (e.g. 30 - 31 = -1)
                    EXTRACT(DAY FROM :date) - EXTRACT(DAY FROM (DATE_TRUNC('month', :date) + INTERVAL '1 month' - INTERVAL '1 day')::date)
                ))
            ))
          )
        """, nativeQuery = true)
    List<PartnerScheduleEntity> findValidSchedulesByDate(
            @Param("action") String action,
            @Param("date") LocalDate date
    );
}
