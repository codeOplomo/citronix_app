package org.anas.citronix.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "harvest_detail")
public class HarvestDetail {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private double quantity;

    @ManyToOne(optional = false)
    private Tree tree;

    @ManyToOne
    @JoinColumn(name = "harvest_id", nullable = true)
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

/*
Post : Harvest {Feild_id  season, date}

=> field_id => field => get trees => foreach =>
        -get Quentity by season => add to HarvestDetail {harvest_id, tree_id, quentity}
        -calc total quentity => add to Harvest {totalQuentity}
 */