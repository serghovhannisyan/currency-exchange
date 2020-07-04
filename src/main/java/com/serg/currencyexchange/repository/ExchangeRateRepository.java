package com.serg.currencyexchange.repository;

import com.serg.currencyexchange.model.ExchangeRate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ExchangeRateRepository extends ReactiveMongoRepository<ExchangeRate, String> {

}
