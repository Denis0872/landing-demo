package com.example.landingdemo.web.dto;

import com.example.landingdemo.domain.model.CaseStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateCaseStatusRequest(@NotNull CaseStatus status) {
}
