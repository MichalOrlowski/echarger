package eu.company.echarger.infrastructure.entrypoints.rest.pricing

import eu.company.echarger.common.IntegrationTest
import eu.company.echarger.core.pricing.PriceRepository
import org.springframework.beans.factory.annotation.Autowired

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class PriceControllerIT extends IntegrationTest {

    private static final String PRICES_ENDPOINT_URL = "/prices"
    private static final String PRICE_DEFINITIONS_ENDPOINT_URL = "/price-definitions"

    @Autowired
    PriceRepository priceRepository

    def "should return proper price after 1 minute of charging when customer is not a vip"() {
        given:
        def customerId = 1

        when:
        createPriceDefinition(preparePriceDefinitionRequestJson("10:00", "12:00"))

        and:
        def response = mockMvc.perform(get(PRICES_ENDPOINT_URL + "/${customerId}/1986-04-08T10:00:00/1986-04-08T10:01:00"))
                .andReturn().response

        then:
        response.status == OK.value()
        response.contentAsString == "10"
    }

    def "should return proper price after 1 minute of charging when customer is a vip"() {
        given:
        def vipId = 2

        when:
        createPriceDefinition(preparePriceDefinitionRequestJson("10:00", "12:00"))

        and:
        def response = mockMvc.perform(get(PRICES_ENDPOINT_URL + "/${vipId}/1986-04-08T10:00:00/1986-04-08T10:01:00"))
                .andReturn().response

        then:
        response.status == OK.value()
        response.contentAsString == "9.0"
    }

    def "should return price == 0 when there is no price definition"() {
        when:
        def response = mockMvc.perform(get(PRICES_ENDPOINT_URL + "/1/1986-04-08T10:00:00/1986-04-08T10:01:00"))
                .andReturn().response

        then:
        response.status == OK.value()
        response.contentAsString == "0"
    }

    def "should return error when wrong request format is provided"() {
        when:
        def response = mockMvc.perform(get(PRICES_ENDPOINT_URL + "/1/wrong/wrong"))
                .andReturn().response

        then:
        response.status == INTERNAL_SERVER_ERROR.value()
    }

    def cleanup() {
        priceRepository.clearAll()
    }

    private def createPriceDefinition(String requestJson) {
        mockMvc.perform(post(PRICE_DEFINITIONS_ENDPOINT_URL)
                .content(requestJson)
                .contentType(APPLICATION_JSON))
                .andReturn().response
    }

    private static def preparePriceDefinitionRequestJson(String durationFrom, String durationTo) {
        return "{     \"pricePerMinute\": 10,\n" +
                "     \"durationFrom\": \"${durationFrom}\",\n" +
                "     \"durationTo\": \"${durationTo}\"\n }"
    }

}
