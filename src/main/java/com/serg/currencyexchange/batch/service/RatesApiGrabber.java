package com.serg.currencyexchange.batch.service;

import com.serg.currencyexchange.batch.dto.RatesApiResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RatesApiGrabber extends CurrencyExchangeApiGrabber<RatesApiResponseDto> {

    public RatesApiGrabber(WebClient.Builder webClientBuilder,
                           @Value("${currency-exchange.rates.base-url}") String baseUrl,
                           @Value("${currency-exchange.rates.path}") String path) {
        super(webClientBuilder, baseUrl, path, RatesApiResponseDto.class);
    }


}
