package org.anas.citronix.service.implementation;

import jakarta.persistence.EntityManager;
import org.anas.citronix.domain.Farm;
import org.anas.citronix.domain.Field;
import org.anas.citronix.exceptions.FarmMaximumFieldsException;
import org.anas.citronix.exceptions.MaximumFieldAreaException;
import org.anas.citronix.exceptions.MinimumFieldAreaException;
import org.anas.citronix.exceptions.FarmNotFoundException;
import org.anas.citronix.repository.FieldRepository;
import org.anas.citronix.service.FieldService;
import org.anas.citronix.service.dto.FieldDTO;
import org.anas.citronix.service.dto.mapper.FieldMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public FieldDTO assignField(FieldDTO fieldDTO, Farm farm) {
        if (fieldDTO.getArea() < 0.1) {
            throw new MinimumFieldAreaException("Field area must be at least 0.1 hectare (1000 m²)");
        }

        double maxFieldArea = farm.getArea() * 0.5;
        if (fieldDTO.getArea() > maxFieldArea) {
            throw new MaximumFieldAreaException("Field area cannot exceed 50% of the farm's total area");
        }

        Field field = fieldMapper.toEntity(fieldDTO);
        field.setFarm(farm);

        farm.getFields().add(field);

        Field savedField = fieldRepository.save(field);

        return fieldMapper.toDTO(savedField);
    }

    @Override
    public Optional<Field> findById(UUID fieldId) {
        return fieldRepository.findById(fieldId);
    }


}
