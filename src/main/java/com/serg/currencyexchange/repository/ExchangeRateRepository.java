package com.serg.currencyexchange.repository;

import com.serg.currencyexchange.model.ExchangeRate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ExchangeRateRepository extends ReactiveMongoRepository<ExchangeRate, String>, CustomExchangeRateRepository {

    Flux<ExchangeRate> findAllByDateAndTime(String date, String time);
}
