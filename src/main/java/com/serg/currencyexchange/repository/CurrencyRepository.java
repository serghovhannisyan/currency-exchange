package com.serg.currencyexchange.repository;

import com.serg.currencyexchange.model.Currency;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CurrencyRepository extends ReactiveMongoRepository<Currency, String> {

    Mono<Currency> findByName(String name);
}
