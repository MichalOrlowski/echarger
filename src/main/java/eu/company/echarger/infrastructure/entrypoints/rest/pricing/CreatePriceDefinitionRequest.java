package eu.company.echarger.infrastructure.entrypoints.rest.pricing;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
class CreatePriceDefinitionRequest {

    private BigDecimal pricePerMinute;
    private LocalTime durationFrom;
    private LocalTime durationTo;
}
