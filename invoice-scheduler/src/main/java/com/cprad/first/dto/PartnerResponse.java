package com.cprad.first.dto;

import java.io.Serializable;

public record PartnerResponse(
        Long id,
        String name,
        String status
) implements Serializable {
}
