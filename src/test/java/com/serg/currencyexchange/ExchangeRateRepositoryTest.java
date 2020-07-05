package com.serg.currencyexchange;

import com.serg.currencyexchange.model.ExchangeProvider;
import com.serg.currencyexchange.model.ExchangeRate;
import com.serg.currencyexchange.repository.ExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class ExchangeRateRepositoryTest {

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ExchangeRateRepository repository;

    @Test
    public void testFindByLatestDateAndProvider() {
        ExchangeRate er1 = getExchangeRate("2020-02-02", "20:20:20", ExchangeProvider.RATES_API);
        ExchangeRate er2 = getExchangeRate("2020-05-05", "20:20:20", ExchangeProvider.RATES_API);
        ExchangeRate er3 = getExchangeRate("2020-08-08", "20:20:20", ExchangeProvider.EXCHANGE_RATE_API);

        Flux<ExchangeRate> exchangeRateFlux = Flux.just(er1, er2, er3).flatMap(repository::save)
                .thenMany(repository.findAllByLatestDateAndProvider(ExchangeProvider.RATES_API));

        StepVerifier.create(exchangeRateFlux)
                .assertNext(next -> {
                    assertThat(next.getProvider()).isEqualByComparingTo(er2.getProvider());
                    assertThat(next.getDate()).isEqualTo(er2.getDate());
                    assertThat(next.getTime()).isEqualTo(er2.getTime());
                })
                .verifyComplete();
    }

    private ExchangeRate getExchangeRate(String date, String time, ExchangeProvider provider) {
        ExchangeRate er1 = new ExchangeRate();
        er1.setBase("USD");
        er1.setDate("2020-02-02");
        er1.setTime("20:20:20");
        er1.setProvider(ExchangeProvider.RATES_API);
        return er1;
    }

}
