package org.anas.citronix.service.implementation;

import org.anas.citronix.domain.Field;
import org.anas.citronix.domain.HarvestDetail;
import org.anas.citronix.domain.Tree;
import org.anas.citronix.domain.enums.Season;
import org.anas.citronix.exceptions.FieldNotFoundException;
import org.anas.citronix.exceptions.HarvestAlreadyExistsException;
import org.anas.citronix.exceptions.HarvestNotFoundException;
import org.anas.citronix.exceptions.TreeNotFoundException;
import org.anas.citronix.repository.HarvestRepository;
import org.anas.citronix.service.FieldService;
import org.anas.citronix.service.HarvestDetailService;
import org.anas.citronix.service.HarvestService;
import org.anas.citronix.domain.Harvest;
import org.anas.citronix.service.TreeService;
import org.anas.citronix.service.dto.FieldPerformanceDTO;
import org.anas.citronix.service.dto.HarvestDTO;
import org.anas.citronix.service.dto.mapper.HarvestMapper;
import org.anas.citronix.utils.HarvestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HarvestServiceImpl implements HarvestService {

    private final HarvestRepository harvestRepository;
    private final HarvestMapper harvestMapper;
    private final TreeService treeService;
    private final FieldService fieldService;
    private final HarvestDetailService harvestDetailService;
    private final HarvestUtils harvestUtils;

    @Autowired
    @Lazy
    public HarvestServiceImpl(HarvestRepository harvestRepository, HarvestMapper harvestMapper, TreeService treeService, FieldService fieldService, HarvestDetailService harvestDetailService, HarvestUtils harvestUtils) {
        this.harvestRepository = harvestRepository;
        this.harvestMapper = harvestMapper;
        this.treeService = treeService;
        this.fieldService = fieldService;
        this.harvestDetailService = harvestDetailService;
        this.harvestUtils = harvestUtils;
    }


    private Field validateFieldAndSeason(Field field, LocalDate harvestDate) {
        Season season = harvestUtils.determineSeason(harvestDate);

        // Extract the year from the harvest date
        int harvestYear = harvestDate.getYear();

        // Check if a harvest exists for the same year and season
        boolean exists = harvestRepository.existsByFieldAndSeasonAndYear(field, season, harvestYear);
        if (exists) {
            throw new HarvestAlreadyExistsException("A harvest already exists for this season in the same year.");
        }

        return field;
    }


    private Harvest createAndSaveHarvest(Field field, LocalDate harvestDate, List<Tree> trees) {
        Harvest harvest = new Harvest();
        harvest.setField(field);
        harvest.setSeason(harvestUtils.determineSeason(harvestDate));
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

    @Override
    public Harvest createHarvest(UUID fieldId, LocalDate harvestDate, List<UUID> treeIds) {

        Field field = fieldService.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("Field not found"));
        // Check if field exists and validate harvest season
        Field validfield = validateFieldAndSeason(field, harvestDate);

        // Fetch and validate trees
        List<Tree> trees = (treeIds == null || treeIds.isEmpty())
                ? treeService.findAllByField(validfield)
                : harvestUtils.validateAndFetchTrees(fieldId, treeIds);

        System.out.println("Trees: " + trees.toString());
        // Create and save harvest
        return createAndSaveHarvest(validfield, harvestDate, trees);
    }

    @Override
    public Harvest createHarvest(UUID fieldId, LocalDate harvestDate) {
        return createHarvest(fieldId, harvestDate, null);
    }

    @Override
    public void deleteHarvest(UUID harvestId) {
        Harvest harvest = harvestRepository.findById(harvestId)
                .orElseThrow(() -> new HarvestNotFoundException("Harvest not found"));

        if (harvest.getDetails() != null && !harvest.getDetails().isEmpty()) {
            for (HarvestDetail detail : harvest.getDetails()) {
                detail.setHarvest(null);
                harvestDetailService.deleteHarvestDetail(detail.getId());
            }
        }

        harvestRepository.delete(harvest);
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

    @Override
    public List<Harvest> getHarvestsByField(UUID fieldId) {
        Field field = fieldService.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("Field not found"));

        return harvestRepository.findAllByField(field);
    }

    @Override
    public Map<Season, Double> calculateTotalHarvestBySeason() {
        List<Harvest> harvests = harvestRepository.findAll();
        return harvests.stream()
                .collect(Collectors.groupingBy(
                        Harvest::getSeason,
                        Collectors.summingDouble(Harvest::getTotalQuantity)
                ));
    }

    @Override
    public Map<UUID, Double> calculateTotalHarvestByField() {
        List<Harvest> harvests = harvestRepository.findAll();
        return harvests.stream()
                .collect(Collectors.groupingBy(
                        harvest -> harvest.getField().getId(),
                        Collectors.summingDouble(Harvest::getTotalQuantity)
                ));
    }

    @Override
    public List<FieldPerformanceDTO> getTopPerformingFields(int limit) {
        Map<UUID, Double> totalHarvestByField = calculateTotalHarvestByField();

        return totalHarvestByField.keySet().stream()
                .sorted((field1, field2) -> Double.compare(totalHarvestByField.get(field2), totalHarvestByField.get(field1)))
                .limit(limit)
                .map(fieldId -> new FieldPerformanceDTO(fieldId, totalHarvestByField.get(fieldId)))
                .collect(Collectors.toList());
    }

}
