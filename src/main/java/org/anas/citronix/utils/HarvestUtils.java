package org.anas.citronix.utils;

import org.anas.citronix.domain.Tree;
import org.anas.citronix.domain.enums.Season;
import org.anas.citronix.exceptions.TreeNotFoundException;
import org.anas.citronix.service.TreeService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class HarvestUtils {

    private final TreeService treeService;

    public HarvestUtils(TreeService treeService) {
        this.treeService = treeService;
    }

    public Season determineSeason(LocalDate harvestDate) {
        int month = harvestDate.getMonthValue();
        if (month == 12 || month == 1 || month == 2) {
            return Season.WINTER;
        } else if (month >= 3 && month <= 5) {
            return Season.SPRING;
        } else if (month >= 6 && month <= 8) {
            return Season.SUMMER;
        } else {
            return Season.AUTUMN;
        }
    }

    public List<Tree> validateAndFetchTrees(UUID fieldId, List<UUID> treeIds) {
        List<Tree> trees = treeService.findAllByIds(treeIds);
        if (trees.isEmpty()) {
            throw new TreeNotFoundException("No trees found for the provided IDs.");
        }

        boolean invalidTrees = trees.stream().anyMatch(tree -> !tree.getField().getId().equals(fieldId));
        if (invalidTrees) {
            throw new IllegalArgumentException("Some trees do not belong to the specified field.");
        }

        return trees;
    }
}

