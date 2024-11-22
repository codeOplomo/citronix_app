package org.anas.citronix.service.dto;

import java.util.UUID;

public class FieldPerformanceDTO {
    private UUID fieldId;
    private double totalQuantity;

    // Constructor
    public FieldPerformanceDTO(UUID fieldId, double totalQuantity) {
        this.fieldId = fieldId;
        this.totalQuantity = totalQuantity;
    }

    // Getters and Setters
    public UUID getFieldId() {
        return fieldId;
    }

    public void setFieldId(UUID fieldId) {
        this.fieldId = fieldId;
    }

    public double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}

