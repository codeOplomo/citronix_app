package org.anas.citronix.web.api;

import jakarta.validation.Valid;
import org.anas.citronix.domain.Sale;
import org.anas.citronix.service.SaleService;
import org.anas.citronix.service.dto.SaleDTO;
import org.anas.citronix.service.dto.mapper.SaleMapper;
import org.anas.citronix.web.vm.SaleVM;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;
    private final SaleMapper saleMapper;

    public SaleController(SaleService saleService, SaleMapper saleMapper) {
        this.saleService = saleService;
        this.saleMapper = saleMapper;
    }


    @PostMapping
    public ResponseEntity<SaleDTO> createSale(@RequestBody @Valid SaleVM saleVM) {
        SaleDTO saleDTO = saleMapper.toDTO(saleVM);

        SaleDTO createdSale = saleService.createSale(saleDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDTO> updateSale(@PathVariable UUID id, @RequestBody @Valid SaleVM saleVM) {
        SaleDTO saleDTO = saleMapper.toDTO(saleVM);
        SaleDTO updatedSale = saleService.updateSale(id, saleDTO);
        return ResponseEntity.ok(updatedSale);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable UUID id) {
        SaleDTO sale = saleService.getSaleById(id);
        return ResponseEntity.ok(sale);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable UUID id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<SaleDTO>> getAllSales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<SaleDTO> salesPage = saleService.getAllSales(page, size, sortBy, sortDir);
        return ResponseEntity.ok(salesPage);
    }

}
