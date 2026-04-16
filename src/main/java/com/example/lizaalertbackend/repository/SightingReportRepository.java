package com.example.lizaalertbackend.repository;

import com.example.lizaalertbackend.domain.model.SightingReport;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SightingReportRepository extends JpaRepository<SightingReport, UUID> {

    List<SightingReport> findAllByLostCaseIdOrderBySeenAtDesc(UUID lostCaseId);
}

