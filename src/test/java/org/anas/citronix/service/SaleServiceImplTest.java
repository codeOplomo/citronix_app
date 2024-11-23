package org.anas.citronix.service;

import org.anas.citronix.domain.Harvest;
import org.anas.citronix.domain.Sale;
import org.anas.citronix.domain.enums.HarvestStatus;
import org.anas.citronix.exceptions.HarvestNotFoundException;
import org.anas.citronix.exceptions.NonProductiveHarvestException;
import org.anas.citronix.exceptions.SaleAlreadyExistsException;
import org.anas.citronix.repository.SaleRepository;
import org.anas.citronix.service.dto.SaleDTO;
import org.anas.citronix.service.dto.mapper.SaleMapper;
import org.anas.citronix.service.implementation.SaleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaleServiceImplTest {

    @Mock
    private SaleRepository saleRepository;
    @Mock
    private HarvestService harvestService;
    @Mock
    private SaleMapper saleMapper;

    @InjectMocks
    private SaleServiceImpl saleService;

    private SaleDTO saleDTO;
    private Harvest harvest;
    private Sale sale;

    @BeforeEach
    void setUp() {
        saleDTO = new SaleDTO();
        saleDTO.setHarvestId(UUID.randomUUID());
        saleDTO.setRevenue(0.0);

        harvest = new Harvest();
        harvest.setId(saleDTO.getHarvestId());
        harvest.setTotalQuantity(100); // Example quantity
        harvest.setStatus(HarvestStatus.AVAILABLE);

        sale = new Sale();
        sale.setId(UUID.randomUUID());
        sale.setHarvest(harvest);
    }

    @Test
    void testCreateSale_HarvestNotFound_ShouldThrowException() {
        when(harvestService.findById(saleDTO.getHarvestId())).thenReturn(Optional.empty());

        assertThrows(HarvestNotFoundException.class, () -> saleService.createSale(saleDTO));

        verify(harvestService).findById(saleDTO.getHarvestId());
        verifyNoInteractions(saleRepository, saleMapper);
    }

    @Test
    void testCreateSale_NonProductiveHarvest_ShouldThrowException() {
        harvest.setTotalQuantity(0);
        when(harvestService.findById(saleDTO.getHarvestId())).thenReturn(Optional.of(harvest));

        assertThrows(NonProductiveHarvestException.class, () -> saleService.createSale(saleDTO));

        verify(harvestService).findById(saleDTO.getHarvestId());
        verifyNoInteractions(saleRepository, saleMapper);
    }

    @Test
    void testCreateSale_SaleAlreadyExists_ShouldThrowException() {
        when(harvestService.findById(saleDTO.getHarvestId())).thenReturn(Optional.of(harvest));
        when(saleRepository.findByHarvestId(saleDTO.getHarvestId())).thenReturn(Optional.of(sale));

        assertThrows(SaleAlreadyExistsException.class, () -> saleService.createSale(saleDTO));

        verify(harvestService).findById(saleDTO.getHarvestId());
        verify(saleRepository).findByHarvestId(saleDTO.getHarvestId());
        verifyNoMoreInteractions(saleRepository);
        verifyNoInteractions(saleMapper);
    }
}
