package com.serg.currencyexchange.batch.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
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
        log.info("path {}, currency {}", path, currency);
        return webClient.get()
                .uri(path, currency)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    log.error("4xx error: {}", clientResponse.bodyToMono(String.class));
                    return Mono.empty();
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("5xx error: {}", clientResponse.bodyToMono(String.class));
                    return Mono.empty();
                })
                .bodyToMono(type);
    }
}
