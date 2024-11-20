package org.anas.citronix.service.implementation;

import jakarta.transaction.Transactional;
import org.anas.citronix.domain.Harvest;
import org.anas.citronix.domain.Sale;
import org.anas.citronix.exceptions.HarvestNotFoundException;
import org.anas.citronix.repository.SaleRepository;
import org.anas.citronix.service.HarvestService;
import org.anas.citronix.service.SaleService;
import org.anas.citronix.service.dto.SaleDTO;
import org.anas.citronix.service.dto.mapper.SaleMapper;
import org.springframework.stereotype.Service;


@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final HarvestService harvestService;
    private final SaleMapper saleMapper;

    public SaleServiceImpl(SaleRepository saleRepository, HarvestService harvestService, SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.harvestService = harvestService;
        this.saleMapper = saleMapper;
    }

    @Transactional
    public SaleDTO createSale(SaleDTO saleDTO) {
        // Find the harvest using the provided harvestId
        Harvest harvest = harvestService.findById(saleDTO.getHarvestId())
                .orElseThrow(() -> new HarvestNotFoundException("Harvest not found"));

        // Calculate the revenue based on the unit price and quantity
        double revenue = saleDTO.getUnitPrice() * harvest.getTotalQuantity();

        Sale sale = saleMapper.toEntity(saleDTO);
        sale.setRevenue(revenue);
        sale.setHarvest(harvest);

        Sale savedSale = saleRepository.save(sale);

        return saleMapper.toDTO(savedSale);
    }
}
