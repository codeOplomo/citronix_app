package org.anas.citronix.service.implementation;

import jakarta.transaction.Transactional;
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
import org.anas.citronix.service.dto.HarvestDetailDTO;
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
        // Check if field exists
        Field field = fieldService.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("Field not found"));

        // Determine the season from the harvest date
        Season season = determineSeason(harvestDate);

        // Check for existing harvest in the same season for the field
        boolean exists = harvestRepository.existsByFieldAndSeason(field, season);
        if (exists) {
            throw new HarvestAlreadyExistsException("A harvest already exists for this season in the field.");
        }

        // Fetch the specified trees
        List<Tree> trees = treeService.findAllByIds(treeIds);
        if (trees.isEmpty()) {
            throw new TreeNotFoundException("No trees found for the provided IDs.");
        }

        // Ensure all trees belong to the specified field
        boolean invalidTrees = trees.stream().anyMatch(tree -> !tree.getField().getId().equals(fieldId));
        if (invalidTrees) {
            throw new IllegalArgumentException("Some trees do not belong to the specified field.");
        }

        // Create harvest
        Harvest harvest = new Harvest();
        harvest.setField(field);
        harvest.setSeason(season);
        harvest.setHarvestDate(harvestDate);

        // Calculate productivity and populate details
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

        // Save harvest and details
        harvest = harvestRepository.save(harvest);
        harvestDetailService.saveAll(details);

        return harvest;
    }
    @Override
    public Harvest createHarvest(UUID fieldId, LocalDate harvestDate) {
        // Check if field exists
        Field field = fieldService.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("Field not found"));

        // Determine the season from the harvest date
        Season season = determineSeason(harvestDate);

        // Check for existing harvest in the same season for the field
        boolean exists = harvestRepository.existsByFieldAndSeason(field, season);
        if (exists) {
            throw new HarvestAlreadyExistsException("A harvest already exists for this season in the field.");
        }

        // Fetch all trees in the field
        List<Tree> trees = treeService.findAllByField(field);
        if (trees.isEmpty()) {
            throw new TreeNotFoundException("No trees found in this field.");
        }

        // Create harvest
        Harvest harvest = new Harvest();
        harvest.setField(field);
        harvest.setSeason(season);
        harvest.setHarvestDate(harvestDate);

        // Calculate productivity and populate details
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

        // Save harvest and details
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
