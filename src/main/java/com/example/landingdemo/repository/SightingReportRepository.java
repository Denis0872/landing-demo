package com.example.landingdemo.repository;

import com.example.landingdemo.domain.model.SightingReport;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SightingReportRepository extends JpaRepository<SightingReport, UUID> {

    List<SightingReport> findAllByLostCaseIdOrderBySeenAtDesc(UUID lostCaseId);
}
