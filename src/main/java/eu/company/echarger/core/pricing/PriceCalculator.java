package eu.company.echarger.core.pricing;

import eu.company.echarger.common.helpers.DateAndTimeHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.time.temporal.ChronoUnit.DAYS;

@Slf4j
class PriceCalculator {

    private static final BigDecimal DEFAULT_DISCOUNT = valueOf(0.1);

    BigDecimal calculate(List<PriceDefinition> priceDefinitionList, LocalDateTime chargingStart, LocalDateTime chargingEnd, boolean vip) {
        log.debug("Calculating price for customer");
        List<DayPeriod> dayPeriods = calculateDayPeriods(chargingStart, chargingEnd);
        BigDecimal price = calculatePriceForPeriods(dayPeriods, priceDefinitionList);

        return vip ? calculateDiscount(price) : price;
    }

    private BigDecimal calculatePriceForPeriods(List<DayPeriod> dayPeriods, List<PriceDefinition> priceDefinitionList) {
        BigDecimal price = ZERO;

        for (PriceDefinition priceDefinition : priceDefinitionList) {
            for (DayPeriod dayPeriod : dayPeriods) {
                long minutesInPeriod = DateAndTimeHelper.getSecondsOverlappingTwoDurations(dayPeriod.start, dayPeriod.end,
                        priceDefinition.getDurationFrom(), priceDefinition.getDurationTo());

                log.debug("Minutes in period [{}] == {}", dayPeriod, price);
                price = price.add(priceDefinition.getPricePerMinute().multiply(valueOf(minutesInPeriod / 60)));
            }
        }

        return price;
    }

    private List<DayPeriod> calculateDayPeriods(LocalDateTime chargingStart, LocalDateTime chargingEnd) {
        List<DayPeriod> dayPeriods = new ArrayList<>();
        long daysBetween = DAYS.between(chargingStart.toLocalDate(), chargingEnd.toLocalDate());

        if (0L == daysBetween) {
            dayPeriods.add(new DayPeriod(chargingStart.toLocalTime(), chargingEnd.toLocalTime()));
        } else {
            LongStream.rangeClosed(0, daysBetween)
                    .forEach(it -> {
                        if (0L == it) {
                            dayPeriods.add(new DayPeriod(chargingStart.toLocalTime(), LocalTime.MAX));
                        } else if (daysBetween != it) {
                            dayPeriods.add(new DayPeriod(LocalTime.MIN, LocalTime.MAX));
                        } else {
                            dayPeriods.add(new DayPeriod(LocalTime.MIN, chargingEnd.toLocalTime()));
                        }
                    });
        }

        return dayPeriods;
    }

    private BigDecimal calculateDiscount(BigDecimal price) {
        log.debug("Calculating discount.");
        return price.subtract(price.multiply(DEFAULT_DISCOUNT));
    }

    @AllArgsConstructor
    private static class DayPeriod {
        private LocalTime start;
        private LocalTime end;
    }

}
