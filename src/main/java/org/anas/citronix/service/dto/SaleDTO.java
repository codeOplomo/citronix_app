package org.anas.citronix.service.dto;

import java.time.LocalDate;
import java.util.UUID;

public class SaleDTO {
    private UUID harvestId;
    private LocalDate date;
    private double unitPrice;
    private String client;
    private double revenue;

    // Getters and setters

    public UUID getHarvestId() {
        return harvestId;
    }

    public void setHarvestId(UUID harvestId) {
        this.harvestId = harvestId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}

