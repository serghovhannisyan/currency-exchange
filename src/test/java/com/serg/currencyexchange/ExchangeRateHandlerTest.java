package com.serg.currencyexchange;

import com.serg.currencyexchange.dto.ExchangeRateResponseDto;
import com.serg.currencyexchange.exception.BadRequestException;
import com.serg.currencyexchange.model.ExchangeProvider;
import com.serg.currencyexchange.service.exchangerate.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureWebTestClient
public class ExchangeRateHandlerTest {

    @MockBean
    private CommandLineRunner commandLineRunner;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ExchangeRateService service;

    @WithMockUser
    @Test
    public void testGetAll() {
        ExchangeRateResponseDto dto1 = new ExchangeRateResponseDto();
        dto1.setBase("USD");
        dto1.setProvider(ExchangeProvider.EXCHANGE_RATE_API);
        ExchangeRateResponseDto dto2 = new ExchangeRateResponseDto();
        dto2.setBase("EUR");
        dto2.setProvider(ExchangeProvider.EXCHANGE_RATE_API);

        given(service.getAll()).willReturn(Flux.just(dto1, dto2));

        webTestClient.get()
                .uri("/api/v1/exchange-rates")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ExchangeRateResponseDto.class)
                .value(dtoList -> {
                    assertThat(dtoList).hasSize(2);

                    assertThat(dtoList.get(0).getBase()).isEqualTo(dto1.getBase());
                    assertThat(dtoList.get(0).getProvider()).isEqualTo(dto1.getProvider());

                    assertThat(dtoList.get(1).getBase()).isEqualTo(dto2.getBase());
                    assertThat(dtoList.get(1).getProvider()).isEqualTo(dto2.getProvider());
                });

    }

    @WithMockUser
    @Test
    public void testGetAllLatest() {
        ExchangeRateResponseDto dto1 = new ExchangeRateResponseDto();
        dto1.setBase("USD");
        dto1.setProvider(ExchangeProvider.EXCHANGE_RATE_API);
        ExchangeRateResponseDto dto2 = new ExchangeRateResponseDto();
        dto2.setBase("EUR");
        dto2.setProvider(ExchangeProvider.EXCHANGE_RATE_API);

        given(service.getLatest()).willReturn(Flux.just(dto1, dto2));

        webTestClient.get()
                .uri("/api/v1/exchange-rates/latest")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ExchangeRateResponseDto.class)
                .value(dtoList -> {
                    assertThat(dtoList).hasSize(2);

                    assertThat(dtoList.get(0).getBase()).isEqualTo(dto1.getBase());
                    assertThat(dtoList.get(0).getProvider()).isEqualTo(dto1.getProvider());

                    assertThat(dtoList.get(1).getBase()).isEqualTo(dto2.getBase());
                    assertThat(dtoList.get(1).getProvider()).isEqualTo(dto2.getProvider());
                });

    }

    @WithMockUser
    @Test
    public void testGetAllLatestByProvider() {
        ExchangeRateResponseDto dto1 = new ExchangeRateResponseDto();
        dto1.setBase("USD");
        dto1.setProvider(ExchangeProvider.EXCHANGE_RATE_API);
        ExchangeRateResponseDto dto2 = new ExchangeRateResponseDto();
        dto2.setBase("EUR");
        dto2.setProvider(ExchangeProvider.RATES_API);

        given(service.getLatestByProvider("RATES_API")).willReturn(Flux.just(dto2));

        webTestClient.get()
                .uri("/api/v1/exchange-rates/latest?provider=RATES_API")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ExchangeRateResponseDto.class)
                .value(dtoList -> {
                    assertThat(dtoList).hasSize(1);

                    assertThat(dtoList.get(0).getBase()).isEqualTo(dto2.getBase());
                    assertThat(dtoList.get(0).getProvider()).isEqualTo(dto2.getProvider());
                });

    }

    @WithMockUser
    @Test
    public void testGetAllLatestByProviderShouldFail() {
        given(service.getLatestByProvider("WRONG_PROVIDER")).willReturn(Flux.error(new BadRequestException()));

        webTestClient.get()
                .uri("/api/v1/exchange-rates/latest?provider=WRONG_PROVIDER")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();

    }
}
