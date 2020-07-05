package com.serg.currencyexchange.handler;

import com.serg.currencyexchange.dto.CurrencyRequestDto;
import com.serg.currencyexchange.dto.CurrencyResponseDto;
import com.serg.currencyexchange.exception.ExceptionMapper;
import com.serg.currencyexchange.service.currency.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Slf4j
public class CurrencyHandler {

    private final CurrencyService currencyService;

    public CurrencyHandler(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        Flux<CurrencyResponseDto> allCurrencies = currencyService.getAll();
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(allCurrencies, CurrencyResponseDto.class);
    }

    public Mono<ServerResponse> add(ServerRequest request) {
        Mono<CurrencyRequestDto> requestDtoMono = request.bodyToMono(CurrencyRequestDto.class);
        return requestDtoMono.flatMap(currencyService::add)
                .flatMap(currencyResponseDto -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(currencyResponseDto))
                .onErrorResume(ExceptionMapper::mapToServerResponse);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<CurrencyRequestDto> currencyRequestDtoMono = request.bodyToMono(CurrencyRequestDto.class);
        return currencyRequestDtoMono.flatMap(dto -> currencyService.update(id, dto))
                .flatMap(currencyResponseDto -> ServerResponse.accepted()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(currencyResponseDto))
                .onErrorResume(ExceptionMapper::mapToServerResponse);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return currencyService.delete(id)
                .flatMap(currencyResponseDto -> ServerResponse.noContent().build())
                .onErrorResume(ExceptionMapper::mapToServerResponse);
    }

}
