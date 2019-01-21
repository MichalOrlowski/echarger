package eu.company.echarger.core.pricing;

import eu.company.echarger.common.ApplicationException;
import eu.company.echarger.common.helpers.DateAndTimeHelper;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.util.List;

@Slf4j
class PriceDefinitionValidator {
    void validate(LocalTime durationFrom, LocalTime durationTo, List<PriceDefinition> priceDefinitions) {
        if (durationFrom.isAfter(durationTo)) {
            log.error("Duration from is after duration to.");
            throw new ApplicationException("Cannot add new Price Definition. Duration from is after duration to.");
        }

        boolean isValid = priceDefinitions.stream()
                .map(priceDefinition -> DateAndTimeHelper.getSecondsOverlappingTwoDurations(durationFrom, durationTo,
                        priceDefinition.getDurationFrom(), priceDefinition.getDurationTo()))
                .noneMatch(seconds -> seconds > 0);

        if (!isValid) {
            log.error("Cannot add new Price Definition. There is overlapping value for price definition.");
            throw new ApplicationException("Cannot add new Price Definition. There is overlapping value for price definition.");
        }

    }
}
