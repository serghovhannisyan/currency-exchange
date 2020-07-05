package com.serg.currencyexchange.batch;

import com.serg.currencyexchange.batch.dto.ExchangeRateApiResponseDto;
import com.serg.currencyexchange.batch.dto.RatesApiResponseDto;
import com.serg.currencyexchange.batch.service.CurrencyExchangeApiGrabber;
import com.serg.currencyexchange.dto.CurrencyResponseDto;
import com.serg.currencyexchange.dto.ExchangeRateRequestDto;
import com.serg.currencyexchange.mapping.ExchangeRateMapper;
import com.serg.currencyexchange.model.ExchangeProvider;
import com.serg.currencyexchange.service.currency.CurrencyService;
import com.serg.currencyexchange.service.exchangerate.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@EnableScheduling
@Configuration
@Slf4j
public class ExchangeRateRetrievalScheduler {

    private final CurrencyExchangeApiGrabber<RatesApiResponseDto> ratesApiGrabber;
    private final CurrencyExchangeApiGrabber<ExchangeRateApiResponseDto> exchangeRateApiGrabber;
    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;
    private final ExchangeRateMapper exchangeRateMapper;

    public ExchangeRateRetrievalScheduler(CurrencyExchangeApiGrabber<RatesApiResponseDto> ratesApiGrabber,
                                          CurrencyExchangeApiGrabber<ExchangeRateApiResponseDto> exchangeRateApiGrabber,
                                          CurrencyService currencyService, ExchangeRateService exchangeRateService,
                                          ExchangeRateMapper exchangeRateMapper) {
        this.ratesApiGrabber = ratesApiGrabber;
        this.exchangeRateApiGrabber = exchangeRateApiGrabber;
        this.currencyService = currencyService;
        this.exchangeRateService = exchangeRateService;
        this.exchangeRateMapper = exchangeRateMapper;
    }

    @Scheduled(initialDelayString = "${app.api-fetch.initial-delay}", fixedDelayString = "${app.api-fetch.fixed-delay}")
    public void run() {
        Flux<CurrencyResponseDto> currenciesFlux = currencyService.getAll().filter(CurrencyResponseDto::isEnabled);

        LocalDateTime currentDate = LocalDateTime.now();
        String date = currentDate.toLocalDate().toString();
        String time = currentDate.toLocalTime().toString();

        fetchRatesApi(currenciesFlux, currentDate);
        fetchExchangeRateApi(currenciesFlux, currentDate);
    }

    private void fetchExchangeRateApi(Flux<CurrencyResponseDto> currenciesFlux, LocalDateTime currentDate) {
        currenciesFlux
                .flatMap(currencyResponseDto -> exchangeRateApiGrabber.retrieveData(currencyResponseDto.getName())
                        .map(dto -> mapToExchangeRateRequestDto(currentDate, dto))
                        .flatMap(exchangeRateService::add)
                        .onErrorResume(e -> {
                            log.error("Failed to retrieve ExchangeRateApi with currency {}", currencyResponseDto.getName(), e);
                            return Mono.empty();
                        })).subscribe();
    }

    private void fetchRatesApi(Flux<CurrencyResponseDto> currenciesFlux, LocalDateTime currentDate) {
        currenciesFlux
                .flatMap(currencyResponseDto -> ratesApiGrabber.retrieveData(currencyResponseDto.getName())
                        .map(dto -> mapToExchangeRateRequestDto(currentDate, dto))
                        .flatMap(exchangeRateService::add)
                        .onErrorResume(e -> {
                            log.error("Failed to retrieve RatesApi with currency {}", currencyResponseDto.getName(), e);
                            return Mono.empty();
                        })).subscribe();
    }

    private ExchangeRateRequestDto mapToExchangeRateRequestDto(LocalDateTime date, RatesApiResponseDto dto) {
        ExchangeRateRequestDto errDto = exchangeRateMapper.mapToExchangeRateRequestDto(dto);
        errDto.setDate(date.toLocalDate().toString());
        errDto.setTime(date.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        errDto.setProvider(ExchangeProvider.RATES_API);
        return errDto;
    }

    private ExchangeRateRequestDto mapToExchangeRateRequestDto(LocalDateTime date, ExchangeRateApiResponseDto dto) {
        ExchangeRateRequestDto errDto = exchangeRateMapper.mapToExchangeRateRequestDto(dto);
        errDto.setDate(date.toLocalDate().toString());
        errDto.setTime(date.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        errDto.setProvider(ExchangeProvider.EXCHANGE_RATE_API);
        return errDto;
    }
}
