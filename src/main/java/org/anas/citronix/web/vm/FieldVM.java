package org.anas.citronix.web.vm;

import jakarta.validation.constraints.Positive;

import java.util.UUID;

public class FieldVM {

    @Positive
    private double area;

    private UUID farmId;

    public UUID getFarmId() {
        return farmId;
    }

    public void setFarmId(UUID farmId) {
        this.farmId = farmId;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }
}
