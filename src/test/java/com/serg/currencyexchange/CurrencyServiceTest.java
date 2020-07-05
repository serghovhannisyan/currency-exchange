package com.serg.currencyexchange;

import com.serg.currencyexchange.mapping.CurrencyMapper;
import com.serg.currencyexchange.model.Currency;
import com.serg.currencyexchange.repository.CurrencyRepository;
import com.serg.currencyexchange.service.currency.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class CurrencyServiceTest {

    // just mocking in order to avoid startup run
    @MockBean
    private CommandLineRunner commandLineRunner;

    @MockBean
    private CurrencyRepository repository;

    @Autowired
    private CurrencyService service;

    @Autowired
    private CurrencyMapper mapper;

    private List<Currency> mockList;

    @BeforeEach
    void beforeEach() {
        this.mockList = Arrays.asList(
                new Currency(null, "USD", true),
                new Currency(null, "EUR", true),
                new Currency(null, "AMD", true));
    }

    @Test
    public void testGetAll() {
        given(repository.findAll()).willReturn(Flux.fromIterable(mockList));

        StepVerifier.create(service.getAll())
                .expectNext(mapper.mapToResponseDto(mockList.get(0)))
                .expectNext(mapper.mapToResponseDto(mockList.get(1)))
                .expectNext(mapper.mapToResponseDto(mockList.get(2)))
                .expectComplete()
                .verify();
    }

    @Test
    public void testSaveOk() {
        given(repository.findByName(any())).willReturn(Mono.empty());
        given(repository.save(any())).willReturn(Mono.just(mockList.get(0)));

        StepVerifier.create(service.add(mapper.mapToCurrencyRequestDto(mockList.get(0))))
                .expectNext(mapper.mapToResponseDto(mockList.get(0)))
                .expectComplete()
                .verify();
    }

    @Test
    public void testSaveShouldFail() {
        Currency currency = new Currency("some_id", "AMD", true);
        given(repository.findByName(any())).willReturn(Mono.just(currency));
        given(repository.save(any())).willReturn(Mono.just(mockList.get(0)));

        StepVerifier.create(service.add(mapper.mapToCurrencyRequestDto(mockList.get(0))))
                .expectError()
                .verify();
    }

    @Test
    public void testDelete() {
        given(repository.delete(any())).willReturn(Mono.empty());
        given(repository.findById(any(String.class))).willReturn(Mono.just(mockList.get(0)));

        StepVerifier.create(service.delete("some_id"))
                .expectNext(mapper.mapToResponseDto(mockList.get(0)))
                .expectComplete()
                .verify();
    }
}
