package org.anas.citronix.service.dto.mapper;

import org.anas.citronix.domain.Farm;
import org.anas.citronix.domain.Field;
import org.anas.citronix.service.dto.FieldDTO;
import org.anas.citronix.web.vm.FieldVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface FieldMapper {
    @Mapping(target = "farm", source = "farmId", qualifiedByName = "farmIdToFarm")
    Field toEntity(FieldDTO fieldDTO);

    FieldDTO toDTO(Field field);

    FieldDTO toDTO(FieldVM fieldVM);

    @Named("farmIdToFarm")
    default Farm farmIdToFarm(UUID farmId) {
        Farm farm = new Farm();
        farm.setId(farmId);
        return farm;
    }
}

