package org.anas.citronix.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "harvest_details")
public class HarvestDetail {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private double quantity;

    @ManyToOne(optional = false)
    private Tree tree;

    @ManyToOne(optional = false)
    private Harvest harvest;

    public HarvestDetail() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public Harvest getHarvest() {
        return harvest;
    }

    public void setHarvest(Harvest harvest) {
        this.harvest = harvest;
    }
}
