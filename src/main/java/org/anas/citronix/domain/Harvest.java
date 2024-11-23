package org.anas.citronix.domain;

import jakarta.persistence.*;
import org.anas.citronix.domain.enums.HarvestStatus;
import org.anas.citronix.domain.enums.Season;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "harvests")
public class Harvest {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Season season;

    @Column(nullable = false)
    private java.time.LocalDate harvestDate;

    @Column(nullable = false)
    private double totalQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HarvestStatus status = HarvestStatus.AVAILABLE;

    @OneToMany(mappedBy = "harvest")
    private List<HarvestDetail> details = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    public Harvest() {
    }

    public double calculateTotalQuantity() {
        return details.stream().mapToDouble(HarvestDetail::getQuantity).sum();
    }

    public void updateTotalQuantity() {
        this.totalQuantity = calculateTotalQuantity();
    }

    // Add helper methods for managing details
    public void addDetail(HarvestDetail detail) {
        details.add(detail);
        detail.setHarvest(this);
        updateTotalQuantity();
    }

    public void removeDetail(HarvestDetail detail) {
        details.remove(detail);
        detail.setHarvest(null);
        updateTotalQuantity();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public List<HarvestDetail> getDetails() {
        return details;
    }

    public void setDetails(List<HarvestDetail> details) {
        this.details = details;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public HarvestStatus getStatus() {
        return status;
    }

    public void setStatus(HarvestStatus status) {
        this.status = status;
    }
}