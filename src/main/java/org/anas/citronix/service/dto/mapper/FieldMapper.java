package org.anas.citronix.service.dto.mapper;

import org.anas.citronix.domain.Field;
import org.anas.citronix.service.dto.FieldDTO;
import org.anas.citronix.web.vm.FieldVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FieldMapper {
 // Automatically map farmId to farm's id
    Field toEntity(FieldDTO fieldDTO);

    FieldDTO toDTO(Field field);

    FieldDTO toDTO(FieldVM fieldVM);
}
