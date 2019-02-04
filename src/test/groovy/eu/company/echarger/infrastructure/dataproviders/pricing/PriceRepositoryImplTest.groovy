package eu.company.echarger.infrastructure.dataproviders.pricing

import eu.company.echarger.core.pricing.PriceDefinition
import spock.lang.Specification

import java.time.LocalTime

class PriceRepositoryImplTest extends Specification {

    PriceRepositoryImpl useCase = new PriceRepositoryImpl()

    def "should save price definition"() {
        given:
        PriceDefinition priceDefinition = new PriceDefinition(LocalTime.NOON, LocalTime.MIDNIGHT, BigDecimal.TEN)

        when:
        Integer id = useCase.save(priceDefinition)

        and:
        PriceDefinition result = useCase.findById(1).get()

        then:
        id == 1
        result.durationFrom == LocalTime.NOON
        result.durationTo == LocalTime.MIDNIGHT
        result.pricePerMinute == BigDecimal.TEN
    }

    def "should return all price definitions"() {
        given:
        PriceDefinition priceDefinition = new PriceDefinition(LocalTime.NOON, LocalTime.MIDNIGHT, BigDecimal.TEN)

        when:
        useCase.save(priceDefinition)
        useCase.save(priceDefinition)

        and:
        def result = useCase.findAll()

        then:
        result.size() == 2
    }

    def "should return empty optional when there's no Price Definition with specified id"() {
        when:
        Optional<PriceDefinition> result = useCase.findById(1)

        then:
        result == Optional.empty()
    }

    def "should clear all price definitions"() {
        given:
        PriceDefinition priceDefinition = new PriceDefinition(LocalTime.NOON, LocalTime.MIDNIGHT, BigDecimal.TEN)

        when:
        useCase.save(priceDefinition)
        useCase.save(priceDefinition)

        and:
        useCase.clearAll()
        def result = useCase.findAll()

        then:
        result.size() == 0
    }
}
