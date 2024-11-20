package org.anas.citronix.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.anas.citronix.domain.enums.Season;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HarvestDTO {
    private UUID id;

    @NotNull
    private UUID fieldId;

    @NotNull
    private Season season;

    @NotNull
    @PastOrPresent
    private LocalDate harvestDate;

    private double totalQuantity;
    private List<HarvestDetailDTO> details = new ArrayList<>();

    // Constructor, getters, and setters

    public HarvestDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFieldId() {
        return fieldId;
    }

    public void setFieldId(UUID fieldId) {
        this.fieldId = fieldId;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public LocalDate getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(LocalDate harvestDate) {
        this.harvestDate = harvestDate;
    }

    public double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public List<HarvestDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<HarvestDetailDTO> details) {
        this.details = details;
    }
}

