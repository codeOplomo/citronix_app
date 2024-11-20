package org.anas.citronix.service.dto.mapper;

import org.anas.citronix.domain.Harvest;
import org.anas.citronix.domain.HarvestDetail;
import org.anas.citronix.service.dto.HarvestDTO;
import org.anas.citronix.service.dto.HarvestDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HarvestMapper {

    @Mapping(target = "details", ignore = true)
    @Mapping(target = "id", ignore = true)
    Harvest toEntity(HarvestDTO harvestDTO);

    HarvestDTO toDTO(Harvest harvest);

    HarvestDetail toEntity(HarvestDetailDTO harvestDetailDTO);

    HarvestDetailDTO toDTO(HarvestDetail harvestDetail);
}

