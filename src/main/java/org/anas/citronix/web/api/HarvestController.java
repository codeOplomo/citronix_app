package org.anas.citronix.web.api;

import jakarta.validation.Valid;
import org.anas.citronix.domain.Harvest;
import org.anas.citronix.service.HarvestService;
import org.anas.citronix.service.dto.HarvestDTO;
import org.anas.citronix.service.dto.HarvestDetailDTO;
import org.anas.citronix.service.dto.mapper.HarvestMapper;
import org.anas.citronix.web.vm.HarvestRequestVM;
import org.anas.citronix.web.vm.HarvestSeasonRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<HarvestDTO>> getHarvestBySeason(@RequestBody @Valid HarvestSeasonRequest request) {
        List<HarvestDTO> harvests = harvestService.getHarvestBySeason(request.getSeason());
        return ResponseEntity.ok(harvests);
    }

}
