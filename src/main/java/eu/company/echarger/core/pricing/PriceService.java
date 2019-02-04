package eu.company.echarger.core.pricing;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository priceRepository;
    private final CustomerRepository customerRepository;

    private PriceCalculator priceCalculator = new PriceCalculator();
    private PriceDefinitionValidator priceDefinitionValidator = new PriceDefinitionValidator();

    public Integer createPriceDefinition(LocalTime durationFrom, LocalTime durationTo, BigDecimal pricePerMinute) {
        priceDefinitionValidator.validate(durationFrom, durationTo, priceRepository.findAll());
        return priceRepository.save(new PriceDefinition(durationFrom, durationTo, pricePerMinute));
    }

    public BigDecimal calculateChargingPrice(Integer customerId, LocalDateTime chargingStart, LocalDateTime chargingEnd) {
        return priceCalculator.calculate(priceRepository.findAll(), chargingStart, chargingEnd, customerRepository.isVip(customerId));
    }

    public PriceDefinition getPriceDefinitionBy(Integer id) {
        return priceRepository.findById(id).orElseThrow(PriceDefinitionNotFoundException::new);
    }
}
