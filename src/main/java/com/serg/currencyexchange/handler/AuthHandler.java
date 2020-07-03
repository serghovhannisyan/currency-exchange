package com.serg.currencyexchange.handler;

import com.serg.currencyexchange.dto.SignInRequestDto;
import com.serg.currencyexchange.dto.SignUpRequestDto;
import com.serg.currencyexchange.exception.AlreadyExistsException;
import com.serg.currencyexchange.exception.BadCredentialsException;
import com.serg.currencyexchange.exception.NotFoundException;
import com.serg.currencyexchange.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Slf4j
public class AuthHandler {

    private final AuthService authService;

    public AuthHandler(AuthService authService) {
        this.authService = authService;
    }

    public Mono<ServerResponse> signUp(ServerRequest request) {
        Mono<SignUpRequestDto> requestDtoMono = request.bodyToMono(SignUpRequestDto.class);
        return requestDtoMono.flatMap(authService::signUp)
                .flatMap(signUpResponseDtoMono -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(signUpResponseDtoMono))
                .onErrorResume(AuthHandler::mapToServerResponse);
    }

    public Mono<ServerResponse> signIn(ServerRequest request) {
        Mono<SignInRequestDto> requestDtoMono = request.bodyToMono(SignInRequestDto.class);
        return requestDtoMono.flatMap(authService::signIn)
                .flatMap(signInResponseDto -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(signInResponseDto))
                .onErrorResume(AuthHandler::mapToServerResponse);
    }

    private static Mono<? extends ServerResponse> mapToServerResponse(Throwable ex) {
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
