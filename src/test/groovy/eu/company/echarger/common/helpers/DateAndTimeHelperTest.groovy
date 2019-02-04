package eu.company.echarger.common.helpers

import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalTime

class DateAndTimeHelperTest extends Specification {

    @Unroll
    def "there should be #expectedResult overlapping seconds between durations"() {
        expect:
        DateAndTimeHelper.getSecondsOverlappingTwoDurations(duration1From, duration1To, duration2From, duration2To) == expectedResult

        where:
        duration1From       | duration1To         | duration2From       | duration2To        || expectedResult
        LocalTime.of(10, 0) | LocalTime.of(12, 0) | LocalTime.of(10, 0) | LocalTime.of(12, 0) | 7200
        LocalTime.of(10, 0) | LocalTime.of(11, 0) | LocalTime.of(10, 0) | LocalTime.of(12, 0) | 3600
        LocalTime.of(11, 0) | LocalTime.of(12, 0) | LocalTime.of(10, 0) | LocalTime.of(12, 0) | 3600
        LocalTime.of(5, 0)  | LocalTime.of(8, 0)  | LocalTime.of(10, 0) | LocalTime.of(12, 0) | 0
        LocalTime.of(5, 0)  | LocalTime.of(18, 0) | LocalTime.of(10, 0) | LocalTime.of(12, 0) | 7200
    }
}
