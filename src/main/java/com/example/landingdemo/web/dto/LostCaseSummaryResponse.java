package com.example.landingdemo.web.dto;

import com.example.landingdemo.domain.model.CaseStatus;
import java.time.Instant;
import java.util.UUID;

public record LostCaseSummaryResponse(
    UUID id,
    CaseStatus status,
    Instant lostAt,
    boolean urgent,
    PetResponse pet,
    LocationDto lastSeenLocation
) {
}
