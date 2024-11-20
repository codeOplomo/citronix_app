package org.anas.citronix.service.dto.mapper;

import org.anas.citronix.domain.Farm;
import org.anas.citronix.service.dto.FarmDTO;
import org.anas.citronix.web.vm.FarmVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface FarmMapper {

    @Mapping(target = "creationDate", ignore = true)
    Farm toEntity(FarmDTO farmDTO);

    @Mapping(target = "creationDate", source = "creationDate")
    FarmDTO toDTO(Farm farm);

    FarmDTO toDTO(FarmVM farmVM);
}

