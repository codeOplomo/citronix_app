package org.anas.citronix.service.dto.mapper;

import org.anas.citronix.domain.Sale;
import org.anas.citronix.service.dto.SaleDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface SaleMapper {

        // Map Sale to SaleDTO
        SaleDTO toDTO(Sale sale);

        // Map SaleDTO to Sale
        Sale toEntity(SaleDTO saleDTO);
}
