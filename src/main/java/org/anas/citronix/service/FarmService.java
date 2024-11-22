package org.anas.citronix.service;

import org.anas.citronix.domain.Farm;
import org.anas.citronix.service.dto.FarmDTO;
import org.anas.citronix.service.dto.FieldDTO;
import org.anas.citronix.web.vm.FieldVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;


public interface FarmService {
    FarmDTO createFarm(FarmDTO farmDTO);

    void deleteFarm(UUID id);

    FarmDTO updateFarm(UUID id, FarmDTO farmDTO);

    FarmDTO getFarmById(UUID id);

    Page<FarmDTO> getAllFarms(Pageable pageable);

    List<FarmDTO> searchFarms(String name, String location);

    FarmDTO addField(FieldDTO fieldDTO);

    Farm findFarmById(UUID id);
}

