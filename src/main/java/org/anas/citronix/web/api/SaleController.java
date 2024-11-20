package org.anas.citronix.web.api;

import jakarta.validation.Valid;
import org.anas.citronix.service.SaleService;
import org.anas.citronix.service.dto.SaleDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<SaleDTO> createSale(@RequestBody @Valid SaleDTO saleDTO) {
        SaleDTO savedSale = saleService.createSale(saleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSale);
    }
}
