package eu.company.echarger.infrastructure.entrypoints.rest.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ApiErrorResponse {

    private HttpStatus status;
    private String message;
}
