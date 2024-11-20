package org.anas.citronix.service.implementation;

import org.anas.citronix.domain.Farm;
import org.anas.citronix.domain.Field;
import org.anas.citronix.exceptions.MaximumFieldAreaException;
import org.anas.citronix.exceptions.MinimumFieldAreaException;
import org.anas.citronix.repository.FieldRepository;
import org.anas.citronix.service.FieldService;
import org.anas.citronix.service.dto.FieldDTO;
import org.anas.citronix.service.dto.mapper.FieldMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FieldServiceImpl implements FieldService {

    private final FieldRepository fieldRepository;
    private final FieldMapper fieldMapper;

    public FieldServiceImpl(FieldRepository fieldRepository, FieldMapper fieldMapper) {
        this.fieldRepository = fieldRepository;
        this.fieldMapper = fieldMapper;
    }

    @Override
    public Optional<Field> findById(UUID fieldId) {
        Optional<Field> field = fieldRepository.findById(fieldId);
        if (field.isPresent()) {
            System.out.println("Field found: " + field.get());
        }
        else {
            System.out.println("Field not found for id: " + fieldId);
        }
        return field;
    }


    @Override
    public FieldDTO createField(FieldDTO fieldDTO) {
        Field field = fieldMapper.toEntity(fieldDTO);
        Field savedField = fieldRepository.save(field);
        return fieldMapper.toDTO(savedField);
    }

    @Override
    public FieldDTO assignField(FieldDTO fieldDTO, Farm farm) {
        if (fieldDTO.getArea() < 0.1) {
            throw new MinimumFieldAreaException("Field area must be at least 0.1 hectare (1000 m²)");
        }

        double maxFieldArea = farm.getArea() * 0.5;
        if (fieldDTO.getArea() > maxFieldArea) {
            throw new MaximumFieldAreaException("Field area cannot exceed 50% of the farm's total area");
        }

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
