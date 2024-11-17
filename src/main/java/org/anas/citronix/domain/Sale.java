package org.anas.citronix.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private java.time.LocalDate date;

    @Column(nullable = false)
    private double unitPrice;

    @Column(nullable = false)
    private String client;

    @Column(nullable = false)
    private double revenue;

    @ManyToOne(optional = false)
    private Harvest harvest;


    public Sale() {
    }

    // Helper Method
    public double calculateRevenue(double quantity) {
        return quantity * unitPrice;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Harvest getHarvest() {
        return harvest;
    }

    public void setHarvest(Harvest harvest) {
        this.harvest = harvest;
    }
}