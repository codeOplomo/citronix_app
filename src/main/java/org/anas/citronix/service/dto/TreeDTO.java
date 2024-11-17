package org.anas.citronix.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.UUID;

public class TreeDTO {
    private UUID id;

    @NotNull(message = "Planting date is required")
    @PastOrPresent(message = "Planting date must be in the past or present")
    private LocalDate plantingDate;

    private int age;
    private double productivity;

    @NotNull(message = "Field ID is required")
    private UUID fieldId;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(LocalDate plantingDate) {
        this.plantingDate = plantingDate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getProductivity() {
        return productivity;
    }

    public void setProductivity(double productivity) {
        this.productivity = productivity;
    }

    public UUID getFieldId() {
        return fieldId;
    }

    public void setFieldId(UUID fieldId) {
        this.fieldId = fieldId;
    }
}

