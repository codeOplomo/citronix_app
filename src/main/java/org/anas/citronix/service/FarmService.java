package org.anas.citronix.service;

import org.anas.citronix.domain.Farm;
import org.anas.citronix.service.dto.FarmDTO;
import org.anas.citronix.service.dto.FieldDTO;

import java.util.List;
import java.util.UUID;


public interface FarmService {
    FarmDTO createFarm(FarmDTO farmDTO);

    FarmDTO updateFarm(UUID id, FarmDTO farmDTO);

    FarmDTO getFarmById(UUID id);

    List<FarmDTO> getAllFarms();

    List<FarmDTO> searchFarms(String name, String location);

    FarmDTO addField(UUID farmId, FieldDTO fieldDTO);
}

