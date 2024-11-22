package org.anas.citronix.web.api;

import jakarta.validation.Valid;
import org.anas.citronix.domain.Harvest;
import org.anas.citronix.domain.enums.Season;
import org.anas.citronix.exceptions.HarvestNotFoundException;
import org.anas.citronix.service.HarvestService;
import org.anas.citronix.service.dto.FieldPerformanceDTO;
import org.anas.citronix.service.dto.HarvestDTO;
import org.anas.citronix.service.dto.mapper.HarvestMapper;
import org.anas.citronix.web.vm.HarvestRequestVM;
import org.anas.citronix.web.vm.HarvestSeasonVM;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/harvests")
public class HarvestController {

    private final HarvestService harvestService;
    private final HarvestMapper harvestMapper;

    public HarvestController(HarvestService harvestService, HarvestMapper harvestMapper) {
        this.harvestService = harvestService;
        this.harvestMapper = harvestMapper;
    }

    @PostMapping
    public ResponseEntity<HarvestDTO> createHarvest(@RequestBody @Valid HarvestRequestVM request) {
        Harvest harvest;
        if (request.getTreeIds() != null && !request.getTreeIds().isEmpty()) {
            harvest = harvestService.createHarvest(request.getFieldId(), request.getHarvestDate(), request.getTreeIds());
        } else {
            harvest = harvestService.createHarvest(request.getFieldId(), request.getHarvestDate());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(harvestMapper.toDTO(harvest));
    }

    @PostMapping("/by-season")
    public ResponseEntity<List<HarvestDTO>> getHarvestBySeason(@RequestBody @Valid HarvestSeasonVM request) {
        List<HarvestDTO> harvests = harvestService.getHarvestBySeason(request.getSeason());
        return ResponseEntity.ok(harvests);
    }

    @GetMapping("/by-field/{fieldId}")
    public ResponseEntity<List<HarvestDTO>> getHarvestsByField(@PathVariable UUID fieldId) {
        List<Harvest> harvests = harvestService.getHarvestsByField(fieldId);
        List<HarvestDTO> harvestDTOs = harvests.stream().map(harvestMapper::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(harvestDTOs);
    }

    @GetMapping("/{harvestId}")
    public ResponseEntity<HarvestDTO> getHarvestById(@PathVariable UUID harvestId) {
        Harvest harvest = harvestService.findById(harvestId)
                .orElseThrow(() -> new HarvestNotFoundException("Harvest not found"));
        return ResponseEntity.ok(harvestMapper.toDTO(harvest));
    }

    @GetMapping("/total-by-season")
    public ResponseEntity<Map<Season, Double>> getTotalHarvestBySeason() {
        Map<Season, Double> totalBySeason = harvestService.calculateTotalHarvestBySeason();
        return ResponseEntity.ok(totalBySeason);
    }

    @GetMapping("/total-by-field")
    public ResponseEntity<Map<UUID, Double>> getTotalHarvestByField() {
        Map<UUID, Double> totalByField = harvestService.calculateTotalHarvestByField();
        return ResponseEntity.ok(totalByField);
    }

    @GetMapping("/top-performing-fields")
    public ResponseEntity<List<FieldPerformanceDTO>> getTopPerformingFields(
            @RequestParam(defaultValue = "5") int limit) {
        List<FieldPerformanceDTO> topFields = harvestService.getTopPerformingFields(limit);
        return ResponseEntity.ok(topFields);
    }


}
