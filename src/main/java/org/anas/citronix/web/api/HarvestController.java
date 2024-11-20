package org.anas.citronix.web.api;

import jakarta.validation.Valid;
import org.anas.citronix.service.HarvestService;
import org.anas.citronix.service.dto.HarvestDTO;
import org.anas.citronix.service.dto.HarvestDetailDTO;
import org.anas.citronix.web.vm.HarvestSeasonRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/harvests")
public class HarvestController {

    private final HarvestService harvestService;

    public HarvestController(HarvestService harvestService) {
        this.harvestService = harvestService;
    }

    @PostMapping
    public ResponseEntity<HarvestDTO> createHarvest(@RequestBody @Valid HarvestDTO harvestDTO) {
        HarvestDTO savedHarvest = harvestService.createHarvest(harvestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHarvest);
    }

    @PostMapping("/by-season")
    public ResponseEntity<List<HarvestDTO>> getHarvestBySeason(@RequestBody @Valid HarvestSeasonRequest request) {
        List<HarvestDTO> harvests = harvestService.getHarvestBySeason(request.getSeason());
        return ResponseEntity.ok(harvests);
    }

}
