package org.anas.citronix.service.dto;

import java.util.UUID;

public class HarvestDetailDTO {
    private UUID id;
    private double quantity;
    private UUID treeId;
    // Constructor, getters, and setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setTreeId(UUID treeId) { this.treeId = treeId;}
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    public UUID getTreeId() {
        return treeId;
    }


}

