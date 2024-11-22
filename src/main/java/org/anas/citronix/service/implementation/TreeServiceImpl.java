package org.anas.citronix.service.implementation;

import org.anas.citronix.domain.Field;
import org.anas.citronix.domain.Tree;
import org.anas.citronix.exceptions.FieldNotFoundException;
import org.anas.citronix.exceptions.TreeDensityExceededException;
import org.anas.citronix.exceptions.TreeNotFoundException;
import org.anas.citronix.repository.TreeRepository;
import org.anas.citronix.service.FieldService;
import org.anas.citronix.service.TreeService;
import org.anas.citronix.service.dto.TreeDTO;
import org.anas.citronix.service.dto.mapper.TreeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TreeServiceImpl implements TreeService {

    private final FieldService fieldService;
    private final TreeMapper treeMapper;
    private final TreeRepository treeRepository;

    @Autowired
    @Lazy
    public TreeServiceImpl(FieldService fieldService, TreeMapper treeMapper, TreeRepository treeRepository) {
        this.fieldService = fieldService;
        this.treeMapper = treeMapper;
        this.treeRepository = treeRepository;
    }


    @Override
    public void deleteTree(UUID treeId) {
        Tree tree = treeRepository.findById(treeId)
                .orElseThrow(() -> new TreeNotFoundException("Tree with ID " + treeId + " not found"));

        if (tree.getField() == null) {
            throw new IllegalStateException("Tree with ID " + treeId + " has no associated field.");
        }

        // Mark the tree as soft deleted
        tree.setRemoved(true);
        treeRepository.save(tree);
    }

    @Override
    public TreeDTO restoreTree(UUID treeId) {
        Tree tree = treeRepository.findById(treeId)
                .orElseThrow(() -> new TreeNotFoundException("Tree with ID " + treeId + " not found"));

        tree.setRemoved(false);
        Tree restoredTree = treeRepository.save(tree); // Save and get the updated tree

        return treeMapper.toDTO(restoredTree); // Map the restored tree to TreeDTO
    }

    @Override
    public Page<TreeDTO> findAllTrees(boolean includeRemoved, Pageable pageable) {
        Page<Tree> treePage = includeRemoved ? treeRepository.findAll(pageable) : treeRepository.findAllByRemoved(false, pageable);

        return treePage.map(treeMapper::toDTO);
    }


    @Override
    public void saveTree(Tree tree) {
        treeRepository.save(tree);
    }

    @Override
    public TreeDTO updateTree(UUID id, TreeDTO treeDTO) {
        Tree existingTree = treeRepository.findById(id)
                .orElseThrow(() -> new TreeNotFoundException("Tree with ID " + id + " not found"));

        Tree updatedTree = treeMapper.toEntity(treeDTO);

        updatedTree.setId(existingTree.getId());

        UUID fieldId = treeDTO.getFieldId();
        Field field = fieldService.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("Field with ID " + fieldId + " not found"));
        updatedTree.setField(field);

        Tree savedTree = treeRepository.save(updatedTree);

        return treeMapper.toDTO(savedTree);
    }


    @Override
    public Optional<Tree> findById(UUID treeId) {
        return treeRepository.findById(treeId);
    }

    @Override
    public List<Tree> findAllByField(Field field) {
        return treeRepository.findAllByField(field);
    }

    @Override
    public List<Tree> findAllByIds(List<UUID> treeIds) {
        return treeRepository.findAllById(treeIds);
    }

    public TreeDTO addTreeToField(TreeDTO treeDTO) {
        UUID fieldId = treeDTO.getFieldId();
        Field field = fieldService.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("Field with ID " + fieldId + " not found"));

        Tree tree = treeMapper.toEntity(treeDTO);

        validateTreeDensity(field);
        tree.setField(field);
        tree.validate();

        Tree savedTree = treeRepository.save(tree);

        return treeMapper.toDTO(savedTree);
    }

    private void validateTreeDensity(Field field) {
        double fieldArea = field.getArea();
        long treeCount = treeRepository.countByField(field);

        double maxDensity = 100 * fieldArea;
        if (treeCount >= maxDensity) {
            throw new TreeDensityExceededException("This field has reached the maximum tree density of " + maxDensity + " trees.");
        }
    }

}
