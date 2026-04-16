package com.example.lizaalertbackend.web.dto;

import com.example.lizaalertbackend.domain.model.MediaType;
import java.util.UUID;

public record MediaAssetResponse(
    UUID id,
    MediaType mediaType,
    String externalUrl,
    String caption,
    Integer sortOrder
) {
}

