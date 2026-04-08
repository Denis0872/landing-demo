package com.example.landingdemo.web.dto;

import com.example.landingdemo.domain.model.PetSex;
import com.example.landingdemo.domain.model.PetSpecies;
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
