package org.anas.citronix.service.implementation;

import org.anas.citronix.domain.Field;
import org.anas.citronix.domain.HarvestDetail;
import org.anas.citronix.domain.Tree;
import org.anas.citronix.domain.enums.Season;
import org.anas.citronix.exceptions.FieldNotFoundException;
import org.anas.citronix.exceptions.HarvestAlreadyExistsException;
import org.anas.citronix.exceptions.TreeNotFoundException;
import org.anas.citronix.repository.HarvestRepository;
import org.anas.citronix.service.FieldService;
import org.anas.citronix.service.HarvestDetailService;
import org.anas.citronix.service.HarvestService;
import org.anas.citronix.domain.Harvest;
import org.anas.citronix.service.TreeService;
import org.anas.citronix.service.dto.HarvestDTO;
import org.anas.citronix.service.dto.mapper.HarvestMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HarvestServiceImpl implements HarvestService {

    private final HarvestRepository harvestRepository;
    private final HarvestMapper harvestMapper;
    private final TreeService treeService;
    private final FieldService fieldService;
    private final HarvestDetailService harvestDetailService;

    public HarvestServiceImpl(HarvestRepository harvestRepository, HarvestMapper harvestMapper, TreeService treeService, FieldService fieldService, HarvestDetailService harvestDetailService) {
        this.harvestRepository = harvestRepository;
        this.harvestMapper = harvestMapper;
        this.treeService = treeService;
        this.fieldService = fieldService;
        this.harvestDetailService = harvestDetailService;
    }

    @Override
    public Harvest createHarvest(UUID fieldId, LocalDate harvestDate, List<UUID> treeIds) {
        // Check if field exists and validate harvest season
        Field field = validateFieldAndSeason(fieldId, harvestDate);

        // Fetch and validate trees
        List<Tree> trees = (treeIds == null || treeIds.isEmpty())
                ? treeService.findAllByField(field)
                : validateAndFetchTrees(fieldId, treeIds);

        // Create and save harvest
        return createAndSaveHarvest(field, harvestDate, trees);
    }

    @Override
    public Harvest createHarvest(UUID fieldId, LocalDate harvestDate) {
        return createHarvest(fieldId, harvestDate, null);
    }

    private Field validateFieldAndSeason(UUID fieldId, LocalDate harvestDate) {
        Field field = fieldService.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("Field not found"));

        Season season = determineSeason(harvestDate);
        boolean exists = harvestRepository.existsByFieldAndSeason(field, season);
        if (exists) {
            throw new HarvestAlreadyExistsException("A harvest already exists for this season in the field.");
        }

        return field;
    }

    private List<Tree> validateAndFetchTrees(UUID fieldId, List<UUID> treeIds) {
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

    private Harvest createAndSaveHarvest(Field field, LocalDate harvestDate, List<Tree> trees) {
        Harvest harvest = new Harvest();
        harvest.setField(field);
        harvest.setSeason(determineSeason(harvestDate));
        harvest.setHarvestDate(harvestDate);

        List<HarvestDetail> details = new ArrayList<>();
        double totalQuantity = 0;
        for (Tree tree : trees) {
            double productivity = tree.calculateProductivity();

            HarvestDetail detail = new HarvestDetail();
            detail.setTree(tree);
            detail.setQuantity(productivity);
            detail.setHarvest(harvest);

            details.add(detail);
            totalQuantity += productivity;
        }

        harvest.setDetails(details);
        harvest.setTotalQuantity(totalQuantity);

        harvest = harvestRepository.save(harvest);
        harvestDetailService.saveAll(details);

        return harvest;
    }

    private Season determineSeason(LocalDate harvestDate) {
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


    @Override
    public Optional<Harvest> findById(UUID harvestId) {
        return harvestRepository.findById(harvestId);
    }

    @Override
    public List<HarvestDTO> getHarvestBySeason(Season season) {
        List<Harvest> harvests = harvestRepository.findBySeason(season);
        return harvests.stream()
                .map(harvestMapper::toDTO)
                .collect(Collectors.toList());
    }

}
