package eu.company.echarger.infrastructure.entrypoints.rest.pricing;

import eu.company.echarger.core.pricing.PriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/price-definitions")
class PriceDefinitionController {

    private static final String CONTROLLER_MAPPING = "/price-definitions";

    private final PriceService priceService;

    @PostMapping
    ResponseEntity<?> createPriceDefinition(@RequestBody CreatePriceDefinitionRequest createPriceDefinitionRequest, UriComponentsBuilder b) {
        UriComponents uriComponents =
                b.path(CONTROLLER_MAPPING + "/{id}").buildAndExpand(priceService.createPriceDefinition(
                        createPriceDefinitionRequest.getDurationFrom(),
                        createPriceDefinitionRequest.getDurationTo(),
                        createPriceDefinitionRequest.getPricePerMinute()));
        log.info("Created new Price Definition");
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @GetMapping("/{priceDefinitionId}")
    ResponseEntity<?> getPriceDefinition(@PathVariable Integer priceDefinitionId) {
        return ResponseEntity.ok().body(priceService.getPriceDefinitionBy(priceDefinitionId));
    }
}
