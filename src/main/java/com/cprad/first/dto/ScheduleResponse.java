package com.cprad.first.dto;

public record ScheduleResponse(
        Long id,
        String action,
        Long partnerId,
        String partnerName,
        String frequency,
        Integer day,
        Boolean isActive
) {
}
