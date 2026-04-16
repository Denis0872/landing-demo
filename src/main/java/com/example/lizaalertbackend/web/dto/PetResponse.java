package com.example.lizaalertbackend.web.dto;

import com.example.lizaalertbackend.domain.model.PetSex;
import com.example.lizaalertbackend.domain.model.PetSpecies;
import java.util.UUID;

public record PetResponse(
    UUID id,
    PetSpecies species,
    String name,
    String breed,
    PetSex sex,
    Integer ageYears,
    String sizeLabel,
    String primaryColor,
    String secondaryColor,
    String specialMarks,
    String microchipId,
    String collarDetails,
    String behaviorNotes
) {
}

