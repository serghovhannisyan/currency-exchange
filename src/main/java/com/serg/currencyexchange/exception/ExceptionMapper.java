package com.serg.currencyexchange.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
public final class ExceptionMapper {

    public static Mono<? extends ServerResponse> mapToServerResponse(Throwable ex) {
        if (ex instanceof BadCredentialsException) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        } else if (ex instanceof NotFoundException) {
            return ServerResponse.notFound().build();
        } else if (ex instanceof AlreadyExistsException) {
            return ServerResponse.status(HttpStatus.CONFLICT).build();
        }

        log.error("Unhandled error: {}", ex.getMessage(), ex);
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
