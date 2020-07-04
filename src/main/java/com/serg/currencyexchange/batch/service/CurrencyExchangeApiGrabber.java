package com.serg.currencyexchange.batch.service;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public abstract class CurrencyExchangeApiGrabber<T> {

    private final WebClient webClient;
    private final Class<T> type;
    private final String path;

    public CurrencyExchangeApiGrabber(WebClient.Builder webClientBuilder,
                                      final String baseUrl,
                                      final String path,
                                      Class<T> type) {
        webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.path = path;
        this.type = type;
    }

    public Mono<T> retrieveData(String currency) {
        return webClient.get()
                .uri(path, currency)
                .retrieve()
                .bodyToMono(type);
    }
}
