package org.anas.citronix.service.implementation;

import jakarta.transaction.Transactional;
import org.anas.citronix.domain.Harvest;
import org.anas.citronix.domain.Sale;
import org.anas.citronix.domain.enums.HarvestStatus;
import org.anas.citronix.exceptions.HarvestNotFoundException;
import org.anas.citronix.exceptions.NonProductiveHarvestException;
import org.anas.citronix.exceptions.SaleAlreadyExistsException;
import org.anas.citronix.exceptions.SaleNotFoundException;
import org.anas.citronix.repository.SaleRepository;
import org.anas.citronix.service.HarvestService;
import org.anas.citronix.service.SaleService;
import org.anas.citronix.service.dto.SaleDTO;
import org.anas.citronix.service.dto.mapper.SaleMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


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

    @Override
    public SaleDTO createSale(SaleDTO saleDTO) {

        Harvest harvest = harvestService.findById(saleDTO.getHarvestId())
                .orElseThrow(() -> new HarvestNotFoundException("Harvest not found with ID: " + saleDTO.getHarvestId()));

        if (harvest.getTotalQuantity() == 0) {
            throw new NonProductiveHarvestException("The trees in this field are not productive, and the total quantity is zero.");
        }

        Optional<Sale> existingSale = saleRepository.findByHarvestId(saleDTO.getHarvestId());
        if (existingSale.isPresent()) {
            throw new SaleAlreadyExistsException("A sale already exists for this harvest.");
        }

        harvest.setStatus(HarvestStatus.SOLD);
        Harvest savedHarvest = harvestService.save(harvest);

        Sale sale = saleMapper.toEntity(saleDTO);

        sale.setHarvest(savedHarvest);

        sale.setDate(java.time.LocalDate.now());

        sale.setRevenue(sale.calculateRevenue(harvest.getTotalQuantity()));

        Sale savedSale = saleRepository.save(sale);

        return saleMapper.toDTO(savedSale);
    }

    @Override
    public SaleDTO updateSale(UUID id, SaleDTO saleDTO) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with ID: " + id));

        Harvest harvest = harvestService.findById(saleDTO.getHarvestId())
                .orElseThrow(() -> new HarvestNotFoundException("Harvest not found with ID: " + saleDTO.getHarvestId()));

        if (harvest.getTotalQuantity() == 0) {
            throw new NonProductiveHarvestException("The trees in this field are not productive, and the total quantity is zero.");
        }

        Harvest oldHarvest = sale.getHarvest();
        oldHarvest.setStatus(HarvestStatus.AVAILABLE);
        harvest.setStatus(HarvestStatus.SOLD);
        Harvest savedHarvest = harvestService.save(harvest);
        Harvest oldSavedHarvest = harvestService.save(oldHarvest);
        sale.setHarvest(savedHarvest);
        sale.setDate(java.time.LocalDate.now());
        sale.setRevenue(sale.calculateRevenue(harvest.getTotalQuantity()));
        sale.setClient(saleDTO.getClient());
        // Update additional fields if necessary

        Sale updatedSale = saleRepository.save(sale);
        return saleMapper.toDTO(updatedSale);
    }

    @Override
    public SaleDTO getSaleById(UUID id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with ID: " + id));
        return saleMapper.toDTO(sale);
    }

    @Override
    public void deleteSale(UUID id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with ID: " + id));

        //Harvest harvest = sale.getHarvest();
        //harvest.setStatus(HarvestStatus.AVAILABLE);
        //harvestService.save(harvest);

        saleRepository.deleteById(id);
    }

    @Override
    public Page<SaleDTO> getAllSales(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Sale> salesPage = saleRepository.findAll(pageable);

        return salesPage.map(saleMapper::toDTO);
    }

}
