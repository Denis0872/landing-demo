package com.example.lizaalertbackend.repository;

import com.example.lizaalertbackend.domain.model.MediaAsset;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaAssetRepository extends JpaRepository<MediaAsset, UUID> {

    List<MediaAsset> findAllByLostCaseIdOrderBySortOrderAsc(UUID lostCaseId);
}

