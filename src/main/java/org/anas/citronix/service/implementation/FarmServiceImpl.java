package org.anas.citronix.service.implementation;

import jakarta.persistence.EntityManager;
import org.anas.citronix.domain.Farm;
import org.anas.citronix.domain.Field;
import org.anas.citronix.exceptions.FarmMaximumFieldsException;
import org.anas.citronix.exceptions.FarmNotFoundException;
import org.anas.citronix.exceptions.NullFarmException;
import org.anas.citronix.repository.FarmRepository;
import org.anas.citronix.service.FarmService;
import org.anas.citronix.service.FieldService;
import org.anas.citronix.service.dto.FarmDTO;
import org.anas.citronix.service.dto.FieldDTO;
import org.anas.citronix.service.dto.mapper.FarmMapper;
import org.anas.citronix.service.dto.mapper.FieldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FarmServiceImpl implements FarmService {

    private final FarmRepository farmRepository;
    private final FarmMapper farmMapper;
    private final FieldMapper fieldMapper;
    private final EntityManager entityManager;
    private final FieldService fieldService;

    @Autowired
    @Lazy
    public FarmServiceImpl(FarmRepository farmRepository, FarmMapper farmMapper, FieldService fieldService, EntityManager entityManager, FieldMapper fieldMapper) {
        this.farmRepository = farmRepository;
        this.farmMapper = farmMapper;
        this.fieldMapper = fieldMapper;
        this.fieldService = fieldService;
        this.entityManager = entityManager;
    }

    @Override
    public void deleteFarm(UUID id) {
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new FarmNotFoundException("Farm with ID " + id + " not found"));

        List<Field> fields = farm.getFields();
        if (fields != null && !fields.isEmpty()) {
            for (Field field : fields) {
                field.setFarm(null);
                fieldService.deleteField(field.getId());
            }
        }

        // Now delete the farm
        farmRepository.delete(farm);
    }

    @Override
    public FarmDTO addField(FieldDTO fieldDTO) {
        Farm farm = farmRepository.findById(fieldDTO.getFarmId())
                .orElseThrow(() -> new FarmNotFoundException("Farm with ID " + fieldDTO.getFarmId() + " not found"));

        if (farm.getFields().size() >= 10) {
            throw new FarmMaximumFieldsException("A farm cannot have more than 10 fields");
        }

        double currentFieldAreaSum = farm.getFieldsAreaSum();


        if (!farm.isValidArea(currentFieldAreaSum + fieldDTO.getArea())) {
            throw new FarmMaximumFieldsException("The sum of the field areas exceeds the farm's total area");
        }

        fieldService.assignField(fieldDTO, farm);

        return farmMapper.toDTO(farm);
    }

    @Override
    public FarmDTO createFarm(FarmDTO farmDTO) {
        if (farmDTO == null) {
            throw new NullFarmException("Farm cannot be null");
        }

        Farm farm = farmMapper.toEntity(farmDTO);
        farm.setCreationDate(java.time.LocalDate.now());

        Farm savedFarm = farmRepository.save(farm);

        if (farmDTO.getFields() != null && !farmDTO.getFields().isEmpty()) {
            List<Field> fields = new ArrayList<>();
            double currentFieldAreaSum = 0.0;

            for (FieldDTO fieldDTO : farmDTO.getFields()) {
                if (fieldDTO.getFarmId() == null) {
                    currentFieldAreaSum += fieldDTO.getArea();
                    if (!farm.isValidArea(currentFieldAreaSum)) {
                        throw new FarmMaximumFieldsException("The sum of the field areas exceeds the farm's total area");
                    }

                    Field field = fieldMapper.toEntity(fieldDTO);
                    field.setFarm(savedFarm);
                    fields.add(field);
                }
            }

            if (!fields.isEmpty()) {
                fieldService.saveAll(fields);
            }

            savedFarm.setFields(fields);
        }

        return farmMapper.toDTO(savedFarm);
    }

    @Override
    public FarmDTO updateFarm(UUID id, FarmDTO farmDTO) {
        if (farmDTO == null) {
            throw new NullFarmException("Farm cannot be null");
        }

        // Find the farm by ID
        Farm existingFarm = farmRepository.findById(id)
                .orElseThrow(() -> new FarmNotFoundException("Farm with ID " + id + " not found"));

        Farm farm = farmMapper.toEntity(farmDTO);

        // Update fields
        existingFarm.setName(farm.getName());
        existingFarm.setLocation(farm.getLocation());
        existingFarm.setArea(farm.getArea());

        // Save the updated farm
        Farm updatedFarm = farmRepository.save(existingFarm);

        // Convert to DTO
        return farmMapper.toDTO(updatedFarm);
    }

    @Override
    public FarmDTO getFarmById(UUID id) {

        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new FarmNotFoundException("Farm with ID " + id + " not found"));

        return farmMapper.toDTO(farm);
    }

    @Override
    public Page<FarmDTO> getAllFarms(Pageable pageable) {
        Page<Farm> farmPage = farmRepository.findAll(pageable);

        return farmPage.map(farmMapper::toDTO);
    }


    @Override
    public List<FarmDTO> searchFarms(String name, String location) {
        if ((name == null || name.isEmpty()) && (location == null || location.isEmpty())) {
            List<Farm> farms = farmRepository.findAll();
            return farms.stream().map(farmMapper::toDTO).collect(Collectors.toList());
        }

        List<Farm> farms = farmRepository.searchFarms(name, location, entityManager);
        return farms.stream().map(farmMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Farm findFarmById(UUID id) {
        return farmRepository.findById(id)
                .orElseThrow(() -> new FarmNotFoundException("Farm with ID " + id + " not found"));
    }
}

