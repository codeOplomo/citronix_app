package org.anas.citronix.web.api;

import jakarta.validation.Valid;
import org.anas.citronix.domain.Field;
import org.anas.citronix.service.FieldService;
import org.anas.citronix.service.TreeService;
import org.anas.citronix.service.dto.FieldDTO;
import org.anas.citronix.service.dto.TreeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/fields")
public class FieldController {

    private final TreeService treeService;
    private final FieldService fieldService;

    public FieldController(TreeService treeService, FieldService fieldService) {
        this.treeService = treeService;
        this.fieldService = fieldService;
    }

    @PostMapping("/assign-tree")
    public ResponseEntity<TreeDTO> addTreeToField(@RequestBody @Valid TreeDTO treeDTO) {
        TreeDTO savedTree = treeService.addTreeToField(treeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTree);
    }

    @PostMapping("/create")
    public ResponseEntity<FieldDTO> createField(@RequestBody @Valid FieldDTO fieldDTO) {
        FieldDTO createdField = fieldService.createField(fieldDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdField);
    }

    @GetMapping("/jibo/{id}")
    public Optional<Field> wachGhayjibo(@PathVariable UUID id){
        return fieldService.findById(id);
    }
}
