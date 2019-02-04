package eu.company.echarger.infrastructure.entrypoints.rest.pricing

import eu.company.echarger.common.IntegrationTest
import eu.company.echarger.core.pricing.PriceRepository
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired

import static org.springframework.http.HttpStatus.*
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class PriceDefinitionControllerIT extends IntegrationTest {

    private static final String PRICE_DEFINITIONS_ENDPOINT_URL = "/price-definitions"
    private static final String LOCATION_HEADER = "Location"

    @Autowired
    PriceRepository priceRepository

    def "should create price definition"() {
        given:
        String requestJson = preparePriceDefinitionRequestJson("10:00", "12:00")

        when:
        def response = createPriceDefinition(requestJson)

        then:
        response.status == CREATED.value()
        response.headers.get(LOCATION_HEADER).getStringValue().endsWith(PRICE_DEFINITIONS_ENDPOINT_URL + "/1")
    }

    def "should get price definition by id"() {
        given:
        String requestJson = preparePriceDefinitionRequestJson("10:00", "12:00")

        when:
        createPriceDefinition(requestJson)

        and:
        def response = mockMvc.perform(get(PRICE_DEFINITIONS_ENDPOINT_URL + "/1"))
                .andReturn().response
        def content = new JsonSlurper().parseText(response.contentAsString)

        then:
        response.status == OK.value()
        content.pricePerMinute == BigDecimal.TEN
        content.durationFrom == "10:00:00"
        content.durationTo == "12:00:00"
    }

    def "should return not found"() {
        when:
        def response = mockMvc.perform(get(PRICE_DEFINITIONS_ENDPOINT_URL + "/666"))
                .andReturn().response

        then:
        response.status == NOT_FOUND.value()
    }

    def "should return error when price definition is overlapping"() {
        when:
        createPriceDefinition(preparePriceDefinitionRequestJson("10:00", "12:00"))

        and:
        def response = createPriceDefinition(preparePriceDefinitionRequestJson("11:00", "12:00"))
        def content = new JsonSlurper().parseText(response.contentAsString)

        then:
        response.status == BAD_REQUEST.value()
        content.message == "Cannot add new Price Definition. There is overlapping value for price definition."
    }

    def "should return error when wrong time format is provided"() {
        when:
        def response = createPriceDefinition(preparePriceDefinitionRequestJson("wrong", "wrong"))
        def content = new JsonSlurper().parseText(response.contentAsString)

        then:
        response.status == INTERNAL_SERVER_ERROR.value()
        content.message.contains("Cannot deserialize value of type `java.time.LocalTime`")
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
