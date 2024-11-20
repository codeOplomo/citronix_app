package org.anas.citronix.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "fields")
public class Field {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private double area;

    @ManyToOne(optional = false)
    private Farm farm;

    @OneToMany(mappedBy = "field")
    private List<Harvest> harvests = new ArrayList<>();

    @OneToMany(mappedBy = "field")
    private List<Tree> trees = new ArrayList<>();

    // Helper Method
    public boolean isTreeDensityValid(int numberOfTrees) {
        return numberOfTrees <= area * 100;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public List<Harvest> getHarvests() {
        return harvests;
    }

    public void setHarvests(List<Harvest> harvests) {
        this.harvests = harvests;
    }

    public List<Tree> getTrees() {
        return trees;
    }

    public void setTrees(List<Tree> trees) {
        this.trees = trees;
    }
}