package org.anas.citronix.service.implementation;

import jakarta.persistence.EntityManager;
import org.anas.citronix.domain.Farm;
import org.anas.citronix.exceptions.FarmNotFoundException;
import org.anas.citronix.exceptions.NullFarmException;
import org.anas.citronix.repository.FarmRepository;
import org.anas.citronix.service.FarmService;
import org.anas.citronix.service.dto.FarmDTO;
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

    public FarmServiceImpl(FarmRepository farmRepository, FarmMapper farmMapper, EntityManager entityManager) {
        this.farmRepository = farmRepository;
        this.farmMapper = farmMapper;
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


}

