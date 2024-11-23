package org.anas.citronix.web.vm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class SaleVM {

    @NotNull
    private UUID harvestId;
    @NotNull
    private double unitPrice;
    @NotNull
    @NotBlank
    private String client;

    // Getters and setters

    public UUID getHarvestId() {
        return harvestId;
    }

    public void setHarvestId(UUID harvestId) {
        this.harvestId = harvestId;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
