package org.anas.citronix.web.vm;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class HarvestRequestVM {

    @NotNull
    private UUID fieldId;
    @NotNull
    private LocalDate harvestDate;

    private List<UUID> treeIds;

    public UUID getFieldId() {
        return fieldId;
    }

    public void setFieldId(UUID fieldId) {
        this.fieldId = fieldId;
    }

    public LocalDate getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(LocalDate harvestDate) {
        this.harvestDate = harvestDate;
    }

    public List<UUID> getTreeIds() {
        return treeIds;
    }

    public void setTreeIds(List<UUID> treeIds) {
        this.treeIds = treeIds;
    }
}
