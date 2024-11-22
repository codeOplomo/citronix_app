package org.anas.citronix.web.vm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.UUID;

public class TreeVM {

    @NotNull(message = "Planting date is required")
    @PastOrPresent(message = "Planting date must be in the past or present")
    private LocalDate plantingDate;

    @NotNull(message = "Field ID is required")
    private UUID fieldId;

    // Getters and Setters

    public LocalDate getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(LocalDate plantingDate) {
        this.plantingDate = plantingDate;
    }

    public UUID getFieldId() {
        return fieldId;
    }

    public void setFieldId(UUID fieldId) {
        this.fieldId = fieldId;
    }
}
