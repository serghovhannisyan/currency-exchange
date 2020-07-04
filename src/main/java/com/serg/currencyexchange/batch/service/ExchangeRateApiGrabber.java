package com.serg.currencyexchange.batch.service;

import com.serg.currencyexchange.batch.dto.ExchangeRateApiResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExchangeRateApiGrabber extends CurrencyExchangeApiGrabber<ExchangeRateApiResponseDto> {

    public ExchangeRateApiGrabber(WebClient.Builder webClientBuilder,
                                  @Value("${currency-exchange.exchange-rate.base-url}") String baseUrl,
                                  @Value("${currency-exchange.exchange-rate.path}") String path) {
        super(webClientBuilder, baseUrl, path,
                ExchangeRateApiResponseDto.class);
    }

}
