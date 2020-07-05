package com.serg.currencyexchange.repository;

import com.serg.currencyexchange.model.ExchangeProvider;
import com.serg.currencyexchange.model.ExchangeRate;
import reactor.core.publisher.Flux;

public interface CustomExchangeRateRepository {

    Flux<ExchangeRate> findAllByLatestDate();

    Flux<ExchangeRate> findAllByLatestDateAndProvider(ExchangeProvider provider);

}
