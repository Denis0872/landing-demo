package com.example.landingdemo.web.dto;

import com.example.landingdemo.domain.model.MediaType;
import java.util.UUID;

public record MediaAssetResponse(
    UUID id,
    MediaType mediaType,
    String externalUrl,
    String caption,
    Integer sortOrder
) {
}
