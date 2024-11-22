package org.anas.citronix.service.implementation;

import org.anas.citronix.domain.*;
import org.anas.citronix.exceptions.FieldNotFoundException;
import org.anas.citronix.exceptions.InsufficientFarmSpaceException;
import org.anas.citronix.exceptions.MaximumFieldAreaException;
import org.anas.citronix.exceptions.MinimumFieldAreaException;
import org.anas.citronix.repository.FieldRepository;
import org.anas.citronix.service.FarmService;
import org.anas.citronix.service.FieldService;
import org.anas.citronix.service.TreeService;
import org.anas.citronix.service.dto.FieldDTO;
import org.anas.citronix.service.dto.mapper.FieldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FieldServiceImpl implements FieldService {

    private final FieldRepository fieldRepository;
    private final FieldMapper fieldMapper;
    private final TreeService treeService;
    private final FarmService farmService;

    @Autowired
    @Lazy
    public FieldServiceImpl(FieldRepository fieldRepository, FieldMapper fieldMapper, TreeService treeService, FarmService farmService) {
        this.fieldRepository = fieldRepository;
        this.fieldMapper = fieldMapper;
        this.treeService = treeService;
        this.farmService = farmService;
    }

    @Override
    public Page<Field> findAll(Pageable pageable) {
        return fieldRepository.findAll(pageable);
    }

    @Override
    public Page<Field> findAllByRemoved(boolean removed, Pageable pageable) {
        return fieldRepository.findByRemoved(removed, pageable);
    }


    @Override
    public void deleteField(UUID fieldId) {
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("Field with ID " + fieldId + " not found"));

        field.setRemoved(true);

        List<Tree> associatedTrees = field.getTrees();
        if (associatedTrees != null && !associatedTrees.isEmpty()) {
            for (Tree tree : associatedTrees) {
                tree.setRemoved(true);
                treeService.saveTree(tree);
            }
        }

        fieldRepository.save(field);
    }

    @Override
    public void restoreField(UUID fieldId) {
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("Field with ID " + fieldId + " not found"));

        field.setRemoved(false);  // Set the field's removed flag to false
        fieldRepository.save(field);  // Save the updated field entity (restore)
    }

    @Override
    public Optional<Field> findById(UUID fieldId) {
        System.out.println("Looking for field with ID: " + fieldId);
        Optional<Field> field = fieldRepository.findById(fieldId);
        if (field.isPresent()) {
            System.out.println("Field found: " + field.get());
        } else {
            System.out.println("Field not found for ID: " + fieldId);
        }
        return field;
    }


    @Override
    public FieldDTO createField(FieldDTO fieldDTO) {
        if (fieldDTO.getArea() < 0.1) {
            throw new MinimumFieldAreaException("Field area must be at least 0.1 hectare (1000 m²)");
        }

        Farm farm = farmService.findFarmById(fieldDTO.getFarmId());

        double maxFieldArea = farm.getArea() * 0.5;
        if (fieldDTO.getArea() > maxFieldArea) {
            throw new MaximumFieldAreaException("Field area cannot exceed 50% of the farm's total area");
        }

        double totalUsedArea = fieldRepository.findAllByFarm(farm).stream()
                .mapToDouble(Field::getArea)
                .sum();

        double availableSpace = farm.getArea() - totalUsedArea;
        if (fieldDTO.getArea() > availableSpace) {
            throw new InsufficientFarmSpaceException("Not enough space left on the farm for this field");
        }

        Field field = fieldMapper.toEntity(fieldDTO);
        field.setFarm(farm);
        Field savedField = fieldRepository.save(field);

        return fieldMapper.toDTO(savedField);
    }

    @Override
    public FieldDTO assignField(FieldDTO fieldDTO, Farm farm) {
        fieldDTO.setFarmId(farm.getId());

        FieldDTO createdField = createField(fieldDTO);

        farm.getFields().add(fieldMapper.toEntity(createdField));

        return createdField;
    }

    @Override
    public void saveAll(List<Field> fields) {
        fieldRepository.saveAll(fields);
    }

}
