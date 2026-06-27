package com.cprad.first.dto;

public record ScheduleRequest(
        String action,
        Long partnerId,
        String frequency,
        Integer day
) {
}
