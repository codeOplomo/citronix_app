package org.anas.citronix.service.dto.mapper;

import org.anas.citronix.domain.Harvest;
import org.anas.citronix.domain.HarvestDetail;
import org.anas.citronix.service.dto.HarvestDTO;
import org.anas.citronix.service.dto.HarvestDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HarvestMapper {

    @Mapping(source = "field.id", target = "fieldId")
    HarvestDTO toDTO(Harvest harvest);

    @Mapping(source = "tree.id", target = "treeId")
    HarvestDetailDTO toDTO(HarvestDetail harvestDetail);

    @Mapping(target = "id", ignore = true)
    Harvest toEntity(HarvestDTO harvestDTO);

    HarvestDetail toEntity(HarvestDetailDTO harvestDetailDTO);
}


