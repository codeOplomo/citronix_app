package org.anas.citronix.web.api;

import jakarta.validation.Valid;
import org.anas.citronix.domain.Field;
import org.anas.citronix.service.FieldService;
import org.anas.citronix.service.TreeService;
import org.anas.citronix.service.dto.FieldDTO;
import org.anas.citronix.service.dto.TreeDTO;
import org.anas.citronix.service.dto.mapper.FieldMapper;
import org.anas.citronix.service.dto.mapper.TreeMapper;
import org.anas.citronix.web.vm.FieldVM;
import org.anas.citronix.web.vm.TreeVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fields")
public class FieldController {

    private final FieldService fieldService;
    private final FieldMapper fieldMapper;

    public FieldController(FieldService fieldService, FieldMapper fieldMapper) {
        this.fieldService = fieldService;
        this.fieldMapper = fieldMapper;
    }



    @PostMapping("/create")
    public ResponseEntity<FieldDTO> createField(@RequestBody @Valid FieldVM fieldVM) {
        FieldDTO fieldDTO = fieldMapper.toDTO(fieldVM);
        FieldDTO createdField = fieldService.createField(fieldDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdField);
    }

    @DeleteMapping("/{fieldId}")
    public ResponseEntity<Void> deleteField(@PathVariable UUID fieldId) {
        fieldService.deleteField(fieldId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/restore/{fieldId}")
    public ResponseEntity<Void> restoreField(@PathVariable UUID fieldId) {
        fieldService.restoreField(fieldId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{fieldId}")
    public ResponseEntity<FieldDTO> getFieldById(@PathVariable UUID fieldId) {
        Optional<Field> field = fieldService.findById(fieldId);
        return field.map(value -> ResponseEntity.ok(fieldMapper.toDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<FieldDTO>> getAllFields(
            @RequestParam(value = "includeRemoved", required = false, defaultValue = "false") boolean includeRemoved,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Pageable pageable = PageRequest.of(page, size,
                sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        Page<Field> fieldPage;
        if (includeRemoved) {
            fieldPage = fieldService.findAll(pageable);
        } else {
            fieldPage = fieldService.findAllByRemoved(false, pageable);
        }

        Page<FieldDTO> fieldDTOPage = fieldPage.map(fieldMapper::toDTO);

        return ResponseEntity.ok(fieldDTOPage);
    }


}
