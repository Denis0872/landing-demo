package com.example.lizaalertbackend.web.dto;

import com.example.lizaalertbackend.domain.model.CaseStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateCaseStatusRequest(@NotNull CaseStatus status) {
}

