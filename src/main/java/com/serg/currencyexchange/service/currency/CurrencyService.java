package com.serg.currencyexchange.service.currency;

import com.serg.currencyexchange.dto.CurrencyRequestDto;
import com.serg.currencyexchange.dto.CurrencyResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyService {

    /**
     * Retrieve all currencies
     *
     * @return {@link Flux} emitting all currencies
     */
    Flux<CurrencyResponseDto> getAll();

    /**
     * Saves a given currency
     *
     * @param dto currency to be saved
     * @return {@link Mono} emitting the saved currency.
     * @throws com.serg.currencyexchange.exception.AlreadyExistsException in case of non unique currency name
     */
    Mono<CurrencyResponseDto> add(CurrencyRequestDto dto);

    /**
     * Updates a given currency
     *
     * @param id of the currency needed to update
     * @param dto object containing all necessary changes
     * @return {@link Mono} emitting the updated currency.
     * @throws com.serg.currencyexchange.exception.NotFoundException in case of given id not found
     */
    Mono<CurrencyResponseDto> update(String id, CurrencyRequestDto dto);

    /**
     * Deletes a given currency
     *
     * @param id of the currency to be deleted
     * @return empty {@link Mono} signaling when operation has completed.
     * @throws com.serg.currencyexchange.exception.NotFoundException in case of given id not found
     */
    Mono<CurrencyResponseDto> delete(String id);
}
