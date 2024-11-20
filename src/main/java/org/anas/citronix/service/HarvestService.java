package org.anas.citronix.service;

import org.anas.citronix.domain.Harvest;
import org.anas.citronix.domain.enums.Season;
import org.anas.citronix.service.dto.HarvestDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HarvestService {

    HarvestDTO createHarvest(HarvestDTO harvestDTO);

    List<HarvestDTO> getHarvestBySeason(Season season);

    Optional<Harvest> findById(UUID harvestId);
}
