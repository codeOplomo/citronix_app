package org.anas.citronix.service;

import org.anas.citronix.service.dto.SaleDTO;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface SaleService {
    SaleDTO createSale(SaleDTO saleDTO);

    SaleDTO updateSale(UUID id, SaleDTO saleDTO);

    SaleDTO getSaleById(UUID id);

    void deleteSale(UUID id);

    Page<SaleDTO> getAllSales(int page, int size, String sortBy, String sortDir);
}
