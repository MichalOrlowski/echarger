package eu.company.echarger.infrastructure.entrypoints.rest.pricing;

import eu.company.echarger.core.pricing.PriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.format.annotation.DateTimeFormat.ISO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/prices")
class PriceController {

    private final PriceService priceService;

    @GetMapping("/{customerId}/{chargingStart}/{chargingEnd}")
    ResponseEntity<BigDecimal> getChargingPrice(@PathVariable Integer customerId,
                                                @PathVariable @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime chargingStart,
                                                @PathVariable @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime chargingEnd) {
        BigDecimal calculatedPrice = priceService.calculateChargingPrice(customerId, chargingStart, chargingEnd);
        log.info("Calculated price: {}", calculatedPrice);
        return ResponseEntity.ok().body(calculatedPrice);
    }
}
