package org.anas.citronix.service.dto;

import java.util.UUID;

public class FieldDTO {
    private double area;

    // Assuming FieldDTO has a farmId property
    private UUID farmId;


    public FieldDTO() {
    }

    public FieldDTO(double area, UUID farmId) {
        this.area = area;
        this.farmId = farmId;
    }

    // Getters and Setters
    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public UUID getFarmId() {
        return farmId;
    }

    public void setFarmId(UUID farmId) {
        this.farmId = farmId;
    }
}

