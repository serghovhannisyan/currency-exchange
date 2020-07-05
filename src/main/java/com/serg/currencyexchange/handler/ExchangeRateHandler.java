package com.serg.currencyexchange.handler;

import com.serg.currencyexchange.dto.ExchangeRateResponseDto;
import com.serg.currencyexchange.exception.ExceptionMapper;
import com.serg.currencyexchange.service.exchangerate.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Slf4j
public class ExchangeRateHandler {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateHandler(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        Optional<String> optionalDate = request.queryParam("date");
        Optional<String> optionalTime = request.queryParam("time");

        if (optionalDate.isPresent() && optionalTime.isPresent()) {
            return exchangeRateService.getAllByDateAndTime(optionalDate.get(), optionalTime.get())
                    .collectList()
                    .flatMap(dtoList -> ServerResponse.ok().bodyValue(dtoList))
                    .onErrorResume(ExceptionMapper::mapToServerResponse);
        }

        Flux<ExchangeRateResponseDto> dto = exchangeRateService.getAll();
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(dto, ExchangeRateResponseDto.class);
    }

    public Mono<ServerResponse> getLatest(ServerRequest request) {
        Optional<String> optionalProvider = request.queryParam("provider");
        if (optionalProvider.isPresent()) {
            return exchangeRateService.getLatestByProvider(optionalProvider.get())
                    .collectList()
                    .flatMap(dtoList -> ServerResponse.ok()
                            .contentType(APPLICATION_JSON)
                            .bodyValue(dtoList))
                    .onErrorResume(ExceptionMapper::mapToServerResponse);
        }

        Flux<ExchangeRateResponseDto> latest = exchangeRateService.getLatest();
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(latest, ExchangeRateResponseDto.class);
    }

}
