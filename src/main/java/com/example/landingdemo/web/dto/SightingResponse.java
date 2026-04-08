package com.example.landingdemo.web.dto;

import com.example.landingdemo.domain.model.ReportStatus;
import java.time.Instant;
import java.util.UUID;

public record SightingResponse(
    UUID id,
    String reporterName,
    String reporterPhone,
    Instant seenAt,
    ReportStatus status,
    Integer confidenceScore,
    LocationDto location,
    String notes
) {
}
