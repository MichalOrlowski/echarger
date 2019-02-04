package eu.company.echarger.common.helpers;

import java.time.Duration;
import java.time.LocalTime;

public final class DateAndTimeHelper {

    private DateAndTimeHelper() {
    }

    public static long getSecondsOverlappingTwoDurations(LocalTime duration1From, LocalTime duration1To,
                                                         LocalTime duration2From, LocalTime duration2To) {
        long priceDefinitionDuration = Duration.between(duration2From, duration2To).getSeconds();
        if (duration2To.equals(LocalTime.MAX)) {
            priceDefinitionDuration++;
        }

        long lowerGapDuration = Duration.between(duration2From, duration1From).getSeconds();
        long higherGapDuration = Duration.between(duration1To, duration2To).getSeconds();

        long secondsInPeriod = 0;
        if (lowerGapDuration > 0 && lowerGapDuration < priceDefinitionDuration || higherGapDuration > 0 && higherGapDuration < priceDefinitionDuration) {
            secondsInPeriod = priceDefinitionDuration;
            if (lowerGapDuration > 0) {
                secondsInPeriod -= lowerGapDuration;
            }
            if (higherGapDuration > 0) {
                secondsInPeriod -= higherGapDuration;
            }
        } else if (lowerGapDuration == 0 || higherGapDuration == 0 || lowerGapDuration < 0 && higherGapDuration < 0) {
            secondsInPeriod = priceDefinitionDuration;
        }

        return secondsInPeriod;
    }
}
