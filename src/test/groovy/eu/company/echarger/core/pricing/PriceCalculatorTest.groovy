package eu.company.echarger.core.pricing

import spock.lang.Specification

import java.time.LocalDateTime
import java.time.LocalTime

class PriceCalculatorTest extends Specification {

    private PriceCalculator useCase = new PriceCalculator()

    def "should return correct value when one price definition and user is not vip"() {
        given:
        def priceDefinitions = [new PriceDefinition(LocalTime.of(12, 0), LocalTime.of(16, 0), BigDecimal.ONE)]
        LocalDateTime chargingStart = LocalDateTime.of(2019, 1, 17, chargingStartHour, 0)
        LocalDateTime chargingEnd = LocalDateTime.of(2019, 1, 17, chargingEndHour, 0)
        boolean vip = false

        expect:
        useCase.calculate(priceDefinitions, chargingStart, chargingEnd, vip) == BigDecimal.valueOf(calculatedPrice)

        where:
        chargingStartHour | chargingEndHour || calculatedPrice
        12                | 15               | 180
        12                | 16               | 240
        13                | 15               | 120
        17                | 20               | 0
        12                | 18               | 240
        10                | 18               | 240
    }

    def "should return correct value when multiple price definitions and user is not vip"() {
        given:
        def priceDefinitions = [new PriceDefinition(LocalTime.of(22, 0), LocalTime.MAX, BigDecimal.TEN), //600
                                new PriceDefinition(LocalTime.of(4, 0), LocalTime.of(22, 0), BigDecimal.ONE), // 60
                                new PriceDefinition(LocalTime.of(0, 0), LocalTime.of(4, 0), BigDecimal.TEN)] //600
        LocalDateTime chargingStart = LocalDateTime.of(2019, 1, 17, chargingStartHour, 0)
        LocalDateTime chargingEnd = LocalDateTime.of(2019, 1, 19, chargingEndHour, 0)
        boolean vip = false

        expect:
        useCase.calculate(priceDefinitions, chargingStart, chargingEnd, vip) == BigDecimal.valueOf(calculatedPrice)

        where:
        chargingStartHour | chargingEndHour || calculatedPrice
        23                | 1                | 600 + 600 + 1200 + 2400 + 18 * 60
    }

    def "should return 10% discounted price when user is vip"() {
        given:
        def priceDefinitions = [new PriceDefinition(LocalTime.of(0, 0), LocalTime.of(4, 0), BigDecimal.ONE)]
        LocalDateTime chargingStart = LocalDateTime.of(2019, 1, 17, 1, 0)
        LocalDateTime chargingEnd = LocalDateTime.of(2019, 1, 17, 2, 0)
        boolean vip = true

        when:
        def result = useCase.calculate(priceDefinitions, chargingStart, chargingEnd, vip)

        then:
        result == 54
    }
}
