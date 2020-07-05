package com.serg.currencyexchange.batch;

import com.serg.currencyexchange.batch.service.Periodic;
import com.serg.currencyexchange.dto.CurrencyResponseDto;
import com.serg.currencyexchange.service.currency.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduler responsible for being triggered periodically.
 * Retrieves currencies from the data source and calls remote APIs in order to fetch new exchange rates
 * Scheduling can be configured via application properties
 *
 * @author Sergey Hovhannisyan
 */
@EnableScheduling
@Configuration
@Slf4j
public class ExchangeRateRetrievalScheduler {

    private final CurrencyService currencyService;
    private final List<Periodic> periodicList;

    public ExchangeRateRetrievalScheduler(CurrencyService currencyService, List<Periodic> periodicList) {
        this.currencyService = currencyService;
        this.periodicList = periodicList;
    }

    @Scheduled(initialDelayString = "${app.api-fetch.initial-delay}", fixedDelayString = "${app.api-fetch.fixed-delay}")
    public void run() {
        final LocalDateTime currentDate = LocalDateTime.now();

        // filter could be done inside db, but as currencies are not too much, for now we can use the same service
        currencyService.getAll()
                .filter(CurrencyResponseDto::isEnabled)
                .map(CurrencyResponseDto::getName)
                .doOnNext(name -> {
                    log.info("Emitted currency: {}", name);
                    periodicList.forEach(periodic -> periodic.handleData(name, currentDate));
                })
                .doOnComplete(() -> log.info("Finished emitting currencies"))
                .subscribe();
    }

}
