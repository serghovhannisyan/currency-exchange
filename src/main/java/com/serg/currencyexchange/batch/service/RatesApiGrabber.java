package com.serg.currencyexchange.batch.service;

import com.serg.currencyexchange.batch.dto.RatesApiResponseDto;
import com.serg.currencyexchange.mapping.ExchangeRateMapper;
import com.serg.currencyexchange.service.exchangerate.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ConditionalOnProperty(name = "currency-exchange.rates.enabled", havingValue = "true")
@Service
@Slf4j
public class RatesApiGrabber extends CurrencyExchangeApiGrabber<RatesApiResponseDto> implements Periodic {

    private final ExchangeRateMapper mapper;
    private final ExchangeRateService exchangeRateService;

    public RatesApiGrabber(WebClient.Builder webClientBuilder,
                           ExchangeRateService exchangeRateService,
                           ExchangeRateMapper mapper,
                           @Value("${currency-exchange.rates.base-url}") String baseUrl,
                           @Value("${currency-exchange.rates.path}") String path) {
        super(webClientBuilder, baseUrl, path, RatesApiResponseDto.class);
        this.mapper = mapper;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public void handleData(String currencyName, LocalDateTime date) {
        retrieveData(currencyName)
                .map(ratesApiResponseDto -> mapper.mapToExchangeRateRequestDto(ratesApiResponseDto, date))
                .flatMap(exchangeRateService::add)
                .onErrorResume(e -> {
                    log.error("Failed to retrieve RatesApi with currency {}", currencyName, e);
                    return Mono.empty();
                }).subscribe();
    }
}
