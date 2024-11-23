package org.anas.citronix.service.dto.mapper;

import org.anas.citronix.domain.Sale;
import org.anas.citronix.service.dto.SaleDTO;
import org.anas.citronix.web.vm.SaleVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface SaleMapper {

        // Map Sale to SaleDTO
        @Mapping(target = "harvestId", source = "harvest.id")
        SaleDTO toDTO(Sale sale);

        // Map SaleDTO to Sale
        Sale toEntity(SaleDTO saleDTO);

        @Mapping(target = "date", ignore = true)
        @Mapping(target = "revenue", ignore = true)
        SaleDTO toDTO(SaleVM saleVM);
}
