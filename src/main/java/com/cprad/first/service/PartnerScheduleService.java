package com.cprad.first.service;

import com.cprad.first.dto.PartnerRequest;
import com.cprad.first.dto.ScheduleRequest;
import com.cprad.first.dto.ScheduleResponse;
import com.cprad.first.entity.BusinessPartnerEntity;
import com.cprad.first.entity.PartnerScheduleEntity;
import com.cprad.first.repository.BusinessPartnerRepository;
import com.cprad.first.repository.PartnerScheduleRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PartnerScheduleService {
    private final BusinessPartnerRepository partnerRepository;
    private final PartnerScheduleRepository scheduleRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public PartnerScheduleService(BusinessPartnerRepository partnerRepository, PartnerScheduleRepository scheduleRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.partnerRepository = partnerRepository;
        this.scheduleRepository = scheduleRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public BusinessPartnerEntity registerPartner(PartnerRequest request) {
        BusinessPartnerEntity partner = new BusinessPartnerEntity();
        partner.setName(request.name());
        partner.setStatus("ACTIVE");
        return partnerRepository.save(partner);
    }

    @Transactional
    public PartnerScheduleEntity registerSchedule(ScheduleRequest request) {
        BusinessPartnerEntity partner = partnerRepository.findById(request.partnerId())
                .orElseThrow(() -> new RuntimeException("Partner not found with ID: " + request.partnerId()));

        PartnerScheduleEntity schedule = new PartnerScheduleEntity();
        schedule.setAction(request.action());
        schedule.setPartner(partner);
        schedule.setFrequency(request.frequency());
        schedule.setDay(request.day());
        schedule.setIsActive(true);

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public PartnerScheduleEntity updateSchedule(Long oldScheduleId, ScheduleRequest request) {
        PartnerScheduleEntity oldSchedule = scheduleRepository.findById(oldScheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found with ID: " + oldScheduleId));

        // Soft delete
        oldSchedule.setIsActive(false);
        scheduleRepository.save(oldSchedule);

        // Create new
        return registerSchedule(request);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getValidSchedulesByDate(String action, LocalDate date) {
        List<PartnerScheduleEntity> schedules = scheduleRepository.findValidSchedulesByDate(action, date);
        return schedules.stream()
                .map(entity -> new ScheduleResponse(
                        entity.getId(),
                        entity.getAction(),
                        entity.getPartner().getId(),
                        entity.getPartner().getName(),
                        entity.getFrequency(),
                        entity.getDay(),
                        entity.getIsActive()
                ))
                .toList();
    }

    public void runDailyInvoiceBatch(String action, LocalDate date) {
        List<PartnerScheduleEntity> validSchedules = scheduleRepository.findValidSchedulesByDate(action, date);

        for (PartnerScheduleEntity schedule : validSchedules) {
            String partnerIdMessage = schedule.getPartner().getId().toString();

            kafkaTemplate.send("invoice-topic", partnerIdMessage);

            System.out.println("========== [PRODUCER] Success push Partner ID " + partnerIdMessage + " to Kafka Topic ==========");
        }
    }
}
