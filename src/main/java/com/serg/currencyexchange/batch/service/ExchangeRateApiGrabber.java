package com.serg.currencyexchange.batch.service;

import com.serg.currencyexchange.batch.dto.ExchangeRateApiResponseDto;
import com.serg.currencyexchange.mapping.ExchangeRateMapper;
import com.serg.currencyexchange.service.exchangerate.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ConditionalOnProperty(name = "currency-exchange.exchange-rate.enabled", havingValue = "true")
@Service
@Slf4j
public class ExchangeRateApiGrabber extends CurrencyExchangeApiGrabber<ExchangeRateApiResponseDto> implements Periodic {

    private final ExchangeRateMapper mapper;
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateApiGrabber(WebClient.Builder webClientBuilder,
                                  ExchangeRateService exchangeRateService,
                                  ExchangeRateMapper mapper,
                                  @Value("${currency-exchange.exchange-rate.base-url}") String baseUrl,
                                  @Value("${currency-exchange.exchange-rate.path}") String path) {
        super(webClientBuilder, baseUrl, path, ExchangeRateApiResponseDto.class);
        this.mapper = mapper;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public void handleData(final String currencyName, final LocalDateTime date) {
        retrieveData(currencyName)
                .map(exchangeRateApiResponseDto -> mapper.mapToExchangeRateRequestDto(exchangeRateApiResponseDto, date))
                .flatMap(exchangeRateService::add)
                .onErrorResume(e -> {
                    log.error("Failed to retrieve ExchangeRateApi with currency {}", currencyName, e);
                    return Mono.empty();
                }).subscribe();
    }
}
