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
    public Optional<Harvest> findById(UUID harvestId) {
        return harvestRepository.findById(harvestId);
    }

    /* harvest en parametres atakhd
    create empty list of details in java
    atboucli ela les fields dialha
    koula field atboucli ela les arbres dialha
    atglss tcreer bnisba lkoula tree detail dialha
    w taddih la list li declariti lfou9

    fash ayssaliw had les boucle atchd QTE total w gt7tha fl harvest
    atched lharvest tsaviha f database
    w takhd lid dialho w t7to fharvest detail
    w tmhi dir repository.save lkola harvest detail

    */
    @Override
    public HarvestDTO createHarvest(HarvestDTO harvestDTO) {
        System.out.println("Field id: {}" + harvestDTO.getFieldId());
        // Validate Field
        UUID fieldId = harvestDTO.getFieldId();
        Field field = fieldService.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("Field not found"));

        System.out.println("Field: {}" + field);
        // Ensure no duplicate harvest for the same season
        if (harvestRepository.existsByFieldAndSeason(field, harvestDTO.getSeason())) {
            throw new HarvestAlreadyExistsException("A harvest already exists for this field and season.");
        }

        // Calculate HarvestDetails
        List<Tree> trees = treeService.findAllByField(field);
        List<HarvestDetail> harvestDetails = new ArrayList<>();
        double totalQuantity = 0;

        for (Tree tree : trees) {
            double quantity = tree.calculateProductivity();

            // Create HarvestDetail with quantity and no Harvest reference
            HarvestDetail detail = new HarvestDetail();
            detail.setTree(tree);
            detail.setQuantity(quantity);
            detail.setHarvest(null);

            harvestDetails.add(detail);
            totalQuantity += quantity;
        }

        // Save HarvestDetails with no Harvest reference
        harvestDetailService.saveAll(harvestDetails);

        // Create Harvest
        Harvest harvest = new Harvest();
        harvest.setSeason(harvestDTO.getSeason());
        harvest.setHarvestDate(harvestDTO.getHarvestDate());
        harvest.setField(field);
        harvest.setTotalQuantity(totalQuantity);

        // Save Harvest
        Harvest savedHarvest = harvestRepository.save(harvest);

        // Update HarvestDetails with Harvest reference
        for (HarvestDetail detail : harvestDetails) {
            detail.setHarvest(savedHarvest);
        }
        harvestDetailService.saveAll(harvestDetails);

        // Return Harvest DTO
        return harvestMapper.toDTO(savedHarvest);
    }


    @Override
    public List<HarvestDTO> getHarvestBySeason(Season season) {
        List<Harvest> harvests = harvestRepository.findBySeason(season);
        return harvests.stream()
                .map(harvestMapper::toDTO)
                .collect(Collectors.toList());
    }

}
