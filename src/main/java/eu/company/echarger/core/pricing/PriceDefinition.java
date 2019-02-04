package eu.company.echarger.core.pricing;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class PriceDefinition {

    private LocalTime durationFrom;
    private LocalTime durationTo;
    private BigDecimal pricePerMinute;
}
