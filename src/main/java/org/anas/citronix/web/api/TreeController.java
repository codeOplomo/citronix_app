package org.anas.citronix.web.api;

import jakarta.validation.Valid;
import org.anas.citronix.service.TreeService;
import org.anas.citronix.service.dto.TreeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/trees")
public class TreeController {

    private final TreeService treeService;

    public TreeController(TreeService treeService) {
        this.treeService = treeService;
    }

    @PostMapping("/assign-tree")
    public ResponseEntity<TreeDTO> addTreeToField(@RequestBody @Valid TreeDTO treeDTO) {
        TreeDTO savedTree = treeService.addTreeToField(treeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTree);
    }

}
