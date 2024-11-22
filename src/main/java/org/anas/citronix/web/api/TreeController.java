package org.anas.citronix.web.api;

import jakarta.validation.Valid;
import org.anas.citronix.domain.Field;
import org.anas.citronix.domain.Tree;
import org.anas.citronix.exceptions.FieldNotFoundException;
import org.anas.citronix.exceptions.TreeNotFoundException;
import org.anas.citronix.service.FieldService;
import org.anas.citronix.service.TreeService;
import org.anas.citronix.service.dto.TreeDTO;
import org.anas.citronix.service.dto.mapper.TreeMapper;
import org.anas.citronix.web.vm.TreeVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trees")
public class TreeController {

    private final TreeService treeService;
    private final TreeMapper treeMapper;
    private final FieldService fieldService;

    public TreeController(TreeService treeService, TreeMapper treeMapper, FieldService fieldService) {
        this.treeService = treeService;
        this.treeMapper = treeMapper;
        this.fieldService = fieldService;
    }


    @PostMapping("/assign-tree")
    public ResponseEntity<TreeDTO> addTreeToField(@RequestBody @Valid TreeVM treeVM) {
        TreeDTO treeDTO = treeMapper.toDTO(treeVM);
        TreeDTO savedTree = treeService.addTreeToField(treeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTree);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TreeDTO> getTreeById(@PathVariable UUID id) {
        Tree tree = treeService.findById(id)
                .orElseThrow(() -> new TreeNotFoundException("Tree with ID " + id + " not found"));
        return ResponseEntity.ok(treeMapper.toDTO(tree));
    }

    @GetMapping("/field/{fieldId}")
    public ResponseEntity<List<TreeDTO>> getTreesByField(@PathVariable UUID fieldId) {
        Field field = fieldService.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("Field with ID " + fieldId + " not found"));

        List<TreeDTO> trees = treeService.findAllByField(field)
                .stream()
                .map(treeMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(trees);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TreeDTO> updateTree(@PathVariable UUID id, @RequestBody @Valid TreeVM treeVM) {
        TreeDTO treeDTO = treeMapper.toDTO(treeVM);
        TreeDTO updatedTree = treeService.updateTree(id, treeDTO);

        return ResponseEntity.ok(updatedTree);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTree(@PathVariable UUID id) {
        treeService.deleteTree(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/restore/{id}")
    public ResponseEntity<TreeDTO> restoreTree(@PathVariable UUID id) {
        TreeDTO restoredTree = treeService.restoreTree(id);
        return ResponseEntity.ok(restoredTree);
    }

    @GetMapping
    public ResponseEntity<Page<TreeDTO>> getAllTrees(
            @RequestParam(value = "includeRemoved", required = false, defaultValue = "false") boolean includeRemoved,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "plantingDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Pageable pageable = PageRequest.of(page, size,
                sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        Page<TreeDTO> treePage = treeService.findAllTrees(includeRemoved, pageable);

        return ResponseEntity.ok(treePage);
    }



}
