package com.serg.currencyexchange.repository;

import com.serg.currencyexchange.model.ExchangeProvider;
import com.serg.currencyexchange.model.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Flux;

public class CustomExchangeRateRepositoryImpl implements CustomExchangeRateRepository {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    private final SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "date", "time");
    private final GroupOperation groupOperation = Aggregation.group("base", "provider").first("$$ROOT").as("doc");
    private final ReplaceRootOperation replaceRootOperation = Aggregation.replaceRoot().withValueOf("doc");

    @Override
    public Flux<ExchangeRate> findAllByLatestDate() {


        Aggregation aggregation = Aggregation.newAggregation(
                sortOperation,
                groupOperation,
                replaceRootOperation
        );

        return reactiveMongoTemplate.aggregate(aggregation, ExchangeRate.class, ExchangeRate.class);
    }

    @Override
    public Flux<ExchangeRate> findAllByLatestDateAndProvider(ExchangeProvider provider) {
        final MatchOperation matchOperation = Aggregation.match(Criteria.where("provider").is(provider.toString()));

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                sortOperation,
                groupOperation,
                replaceRootOperation
        );

        return reactiveMongoTemplate.aggregate(aggregation, ExchangeRate.class, ExchangeRate.class);
    }

}
