package com.serg.currencyexchange.service.exchangerate;

import com.serg.currencyexchange.dto.ExchangeRateRequestDto;
import com.serg.currencyexchange.dto.ExchangeRateResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeRateService {

    Flux<ExchangeRateResponseDto> getAll();

    Flux<ExchangeRateResponseDto> getAllByDateAndTime(String date, String time);

    Flux<ExchangeRateResponseDto> getLatest();

    Flux<ExchangeRateResponseDto> getLatestByProvider(String provider);

    Mono<ExchangeRateResponseDto> add(ExchangeRateRequestDto dto);
}
