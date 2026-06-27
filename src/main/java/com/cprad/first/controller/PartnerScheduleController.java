package com.cprad.first.controller;

import com.cprad.first.dto.PartnerRequest;
import com.cprad.first.dto.ScheduleRequest;
import com.cprad.first.dto.ScheduleResponse;
import com.cprad.first.entity.BusinessPartnerEntity;
import com.cprad.first.entity.PartnerScheduleEntity;
import com.cprad.first.service.PartnerScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PartnerScheduleController {
    private final PartnerScheduleService scheduleService;

    public PartnerScheduleController(PartnerScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 1. Endpoint Register Business Partner
    // POST http://localhost:8080/api/v1/partners
    @PostMapping("/partners")
    public ResponseEntity<BusinessPartnerEntity> registerPartner(@RequestBody PartnerRequest request) {
        BusinessPartnerEntity partner = scheduleService.registerPartner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(partner);
    }

    // 2. Endpoint Register Schedule
    // POST http://localhost:8080/api/v1/schedules
    @PostMapping("/schedules")
    public ResponseEntity<PartnerScheduleEntity> registerSchedule(@RequestBody ScheduleRequest request) {
        PartnerScheduleEntity schedule = scheduleService.registerSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(schedule);
    }

    // 3. Endpoint Update Schedule (Deactivate Old + Create New)
    // PUT http://localhost:8080/api/v1/schedules/{id}
    @PutMapping("/schedules/{id}")
    public ResponseEntity<PartnerScheduleEntity> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequest request) {
        PartnerScheduleEntity updatedSchedule = scheduleService.updateSchedule(id, request);
        return ResponseEntity.ok(updatedSchedule);
    }

    // 4. Endpoint Find Valid Schedules by Date
    // GET http://localhost:8080/api/v1/schedules/valid?action=SEND_INVOICE&date=2026-06-30
    @GetMapping("/schedules/valid")
    public ResponseEntity<List<ScheduleResponse>> getValidSchedules(
            @RequestParam String action,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<ScheduleResponse> validSchedules = scheduleService.getValidSchedulesByDate(action, date);
        return ResponseEntity.ok(validSchedules);
    }
}
