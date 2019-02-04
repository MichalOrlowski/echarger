package eu.company.echarger.infrastructure.entrypoints.rest.exception;

import eu.company.echarger.common.ApplicationException;
import eu.company.echarger.core.pricing.PriceDefinitionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity handleApplicationException(ApplicationException e) {
        log.error(e.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getLocalizedMessage())
                .build();

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler({PriceDefinitionNotFoundException.class})
    public ResponseEntity handleApplicationException(PriceDefinitionNotFoundException e) {
        log.error(e.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(e.getLocalizedMessage())
                .build();

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleCheckedException(Exception e) {
        log.error(e.getMessage());
        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getLocalizedMessage())
                .build();

        return new ResponseEntity<>(response, response.getStatus());
    }
}
