package eu.company.echarger.core.pricing

import eu.company.echarger.common.ApplicationException
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalTime

class PriceDefinitionValidatorTest extends Specification {

    private static final OVERLAPPED_EXCEPTION_MESSAGE = "Cannot add new Price Definition. There is overlapping value for price definition."
    private static final WRONG_TIME_EXPECTED_EXCEPTION_MESSAGE = "Cannot add new Price Definition. Duration from is after duration to."

    private PriceDefinitionValidator useCase = new PriceDefinitionValidator()

    @Unroll
    def "should throw exception when durationFrom is #durationFrom and durationTo is #durationTo"() {
        given:
        def priceDefinitions = [new PriceDefinition(LocalTime.of(3, 0), LocalTime.of(4, 0), BigDecimal.ONE),
                                new PriceDefinition(LocalTime.of(12, 0), LocalTime.of(16, 0), BigDecimal.ONE)]

        when:
        useCase.validate(durationFrom, durationTo, priceDefinitions)

        then:
        def error = thrown(ApplicationException.class)
        error.message == OVERLAPPED_EXCEPTION_MESSAGE

        where:
        durationFrom        | durationTo
        LocalTime.of(12, 0) | LocalTime.of(16, 0)
        LocalTime.of(11, 0) | LocalTime.of(13, 0)
        LocalTime.of(15, 0) | LocalTime.of(17, 0)
    }

    @Unroll
    def "should not throw exception when durationFrom is #durationFrom and durationTo is #durationTo"() {
        given:
        def priceDefinitions = [new PriceDefinition(LocalTime.of(3, 0), LocalTime.of(4, 0), BigDecimal.ONE),
                                new PriceDefinition(LocalTime.of(12, 0), LocalTime.of(16, 0), BigDecimal.ONE)]

        when:
        useCase.validate(durationFrom, durationTo, priceDefinitions)

        then:
        noExceptionThrown()

        where:
        durationFrom        | durationTo
        LocalTime.of(8, 0)  | LocalTime.of(11, 0)
        LocalTime.of(17, 0) | LocalTime.of(19, 30)
        LocalTime.of(11, 0) | LocalTime.of(12, 0)
        LocalTime.of(16, 0) | LocalTime.of(18, 0)
    }

    @Unroll
    def "should throw exception when durationFrom is after durationTo"() {
        given:
        def priceDefinitions = [new PriceDefinition(LocalTime.of(3, 0), LocalTime.of(4, 0), BigDecimal.ONE)]

        when:
        useCase.validate(LocalTime.of(16, 0), LocalTime.of(12, 0), priceDefinitions)

        then:
        def error = thrown(ApplicationException.class)
        error.message == WRONG_TIME_EXPECTED_EXCEPTION_MESSAGE
    }

}
