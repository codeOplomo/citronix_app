package org.anas.citronix.service.implementation;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FarmServiceImpl implements FarmService {

    private final FarmRepository farmRepository;
    private final FarmMapper farmMapper;
    private final EntityManager entityManager;
    private final FieldService fieldService;

    public FarmServiceImpl(FarmRepository farmRepository, FarmMapper farmMapper, FieldService fieldService, EntityManager entityManager) {
        this.farmRepository = farmRepository;
        this.farmMapper = farmMapper;
        this.fieldService = fieldService;
        this.entityManager = entityManager;
    }

    @Override
    public FarmDTO createFarm(FarmDTO farmDTO) {
        if (farmDTO == null) {
            throw new NullFarmException("Farm cannot be null");
        }
        Farm farm = farmMapper.toEntity(farmDTO);

        farm.setCreationDate(java.time.LocalDate.now());

        Farm savedFarm = farmRepository.save(farm);
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
    public List<FarmDTO> getAllFarms() {
        List<Farm> farms = farmRepository.findAll();

        return farms.stream()
                .map(farmMapper::toDTO)
                .collect(Collectors.toList());
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
    public FarmDTO addField(UUID farmId, FieldDTO fieldDTO) {
        Farm farm = farmRepository.findById(farmId)
                .orElseThrow(() -> new FarmNotFoundException("Farm with ID " + farmId + " not found"));

        // Check farm constraints before adding field
        if (farm.getFields().size() >= 10) {
            throw new FarmMaximumFieldsException("A farm cannot have more than 10 fields");
        }

        double currentFieldAreaSum = farm.getFields().stream()
                .mapToDouble(Field::getArea)
                .sum();

        if (!farm.isValidArea(currentFieldAreaSum + fieldDTO.getArea())) {
            throw new FarmMaximumFieldsException("The sum of the field areas exceeds the farm's total area");
        }

        fieldService.assignField(fieldDTO, farm);

        return farmMapper.toDTO(farm);
    }


}

