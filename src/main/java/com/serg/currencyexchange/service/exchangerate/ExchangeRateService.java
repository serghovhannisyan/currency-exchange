package com.serg.currencyexchange.service.exchangerate;

import com.serg.currencyexchange.dto.ExchangeRateRequestDto;
import com.serg.currencyexchange.dto.ExchangeRateResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeRateService {

    /**
     * Retrieve all exchange rates
     *
     * @return {@link Flux} emitting all exchange rates
     */
    Flux<ExchangeRateResponseDto> getAll();

    /**
     * Retrieve all exchange rates based on date and time
     *
     * @param date of the exchange rate been updated. Format "HH:mm:ss"
     * @param time of the exchange rate been updated. Format "yyyy-MM-dd"
     * @return @return {@link Flux} emitting all exchange rates filtered by date and time
     */
    Flux<ExchangeRateResponseDto> getAllByDateAndTime(String date, String time);

    /**
     * Retrieves only last updated exchange rates
     *
     * @return {@link Flux} emitting latest exchange rates
     */
    Flux<ExchangeRateResponseDto> getLatest();

    /**
     * Retrieves latest updated exchange rates by given provider
     *
     * @param provider name of the provider {@link com.serg.currencyexchange.model.ExchangeProvider}
     * @return {@link Flux} emitting latest exchange rates based on provider
     */
    Flux<ExchangeRateResponseDto> getLatestByProvider(String provider);

    /**
     * Saves a given exchange rate, used for historical requests
     *
     * @param dto exchange rate to be saved
     * @return {@link Mono} emitting the saved exchange rate.
     */
    Mono<ExchangeRateResponseDto> add(ExchangeRateRequestDto dto);
}
