package com.serg.currencyexchange.service.exchangerate;

import com.serg.currencyexchange.dto.ExchangeRateRequestDto;
import com.serg.currencyexchange.dto.ExchangeRateResponseDto;
import com.serg.currencyexchange.model.ExchangeProvider;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface ExchangeRateService {

    Flux<ExchangeRateResponseDto> getAll();

    Flux<ExchangeRateResponseDto> getAllByDate(LocalDateTime date);

    Flux<ExchangeRateResponseDto> getAllByDateGreaterThan(LocalDateTime date);

    Flux<ExchangeRateResponseDto> getLatest();

    Flux<ExchangeRateResponseDto> getLatestByProvider(ExchangeProvider provider);

    Flux<ExchangeRateResponseDto> getLatestByBase(String base);

    Mono<ExchangeRateResponseDto> add(ExchangeRateRequestDto dto);
}
