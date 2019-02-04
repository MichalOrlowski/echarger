package eu.company.echarger.infrastructure.dataproviders.pricing

import spock.lang.Specification
import spock.lang.Unroll

class CustomerRepositoryImplTest extends Specification {

    CustomerRepositoryImpl useCase = new CustomerRepositoryImpl()

    @Unroll
    def "should return #expectedResponse when customerId is #customerId"() {
        expect:
        useCase.isVip(customerId) == expectedResponse

        where:
        customerId || expectedResponse
        0          || true
        1          || false
        2          || true
    }
}
