package com.serg.currencyexchange.service.currency;

import com.serg.currencyexchange.dto.CurrencyRequestDto;
import com.serg.currencyexchange.dto.CurrencyResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyService {

    Flux<CurrencyResponseDto> getAll();

    Mono<CurrencyResponseDto> add(CurrencyRequestDto dto);

    Mono<CurrencyResponseDto> update(String id, CurrencyRequestDto dto);

    Mono<CurrencyResponseDto> delete(String id);
}
