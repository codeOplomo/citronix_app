package org.anas.citronix.web.api;

import jakarta.validation.Valid;
import org.anas.citronix.service.FarmService;
import org.anas.citronix.service.dto.FarmDTO;
import org.anas.citronix.service.dto.FieldDTO;
import org.anas.citronix.service.dto.mapper.FarmMapper;
import org.anas.citronix.service.dto.mapper.FieldMapper;
import org.anas.citronix.web.vm.FarmVM;
import org.anas.citronix.web.vm.FieldVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/farms")
public class FarmController {

    private final FarmService farmService;
    private final FarmMapper farmMapper;
    private final FieldMapper fieldMapper;

    public FarmController(FarmService farmService, FarmMapper farmMapper, FieldMapper fieldMapper) {
        this.farmService = farmService;
        this.farmMapper = farmMapper;
        this.fieldMapper = fieldMapper;
    }

    // Create a new farm
    @PostMapping
    public ResponseEntity<FarmDTO> createFarm(@Valid @RequestBody FarmVM farmVM) {
        FarmDTO farmDTO = farmMapper.toDTO(farmVM);

        if (farmVM.getFields() != null && !farmVM.getFields().isEmpty()) {
            for (FieldVM fieldVM : farmVM.getFields()) {
                FieldDTO fieldDTO = fieldMapper.toDTO(fieldVM);
                farmDTO.getFields().add(fieldDTO);
            }
        }

        FarmDTO createdFarmDTO = farmService.createFarm(farmDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFarmDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFarm(@PathVariable UUID id) {
        farmService.deleteFarm(id);
        String message = "Farm with ID " + id + " has been successfully deleted.";
        return ResponseEntity.ok(message);
    }


    // Update an existing farm
    @PutMapping("/{id}")
    public ResponseEntity<FarmDTO> updateFarm(@PathVariable UUID id, @Valid @RequestBody FarmVM farmVM) {
        FarmDTO farmDTO = farmMapper.toDTO(farmVM);
        FarmDTO updatedFarmDTO = farmService.updateFarm(id, farmDTO);
        return ResponseEntity.ok(updatedFarmDTO);
    }

    // Get details of a farm by ID
    @GetMapping("/{id}")
    public ResponseEntity<FarmDTO> getFarmById(@PathVariable UUID id) {
        FarmDTO farmDTO = farmService.getFarmById(id);
        return ResponseEntity.ok(farmDTO);
    }

    // Get all farms (optional pagination)
    @GetMapping
    public ResponseEntity<Page<FarmDTO>> getAllFarms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Pageable pageable = PageRequest.of(page, size,
                sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        Page<FarmDTO> farmPage = farmService.getAllFarms(pageable);

        return ResponseEntity.ok(farmPage);
    }


    @GetMapping("/search")
    public ResponseEntity<List<FarmDTO>> searchFarms(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "location", required = false) String location) {

        List<FarmDTO> farms = farmService.searchFarms(name, location);
        return ResponseEntity.ok(farms);
    }

    @PostMapping("/{farmId}/fields")
    public ResponseEntity<FarmDTO> addFieldToFarm(@PathVariable UUID farmId, @Valid @RequestBody FieldVM fieldVM) {
        FieldDTO fieldDTO = fieldMapper.toDTO(fieldVM);
        fieldDTO.setFarmId(farmId);
        FarmDTO updatedFarmDTO = farmService.addField(fieldDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedFarmDTO);
    }
}

