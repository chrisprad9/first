package com.cprad.first.dto;

import java.io.Serializable;

public record ScheduleResponse(
        Long id,
        String action,
        Long partnerId,
        String partnerName,
        String frequency,
        Integer day,
        Boolean isActive
) implements Serializable {
}
