package com.serg.currencyexchange.mapping;

import com.serg.currencyexchange.batch.dto.ExchangeRateApiResponseDto;
import com.serg.currencyexchange.batch.dto.RatesApiResponseDto;
import com.serg.currencyexchange.dto.ExchangeRateRequestDto;
import com.serg.currencyexchange.dto.ExchangeRateResponseDto;
import com.serg.currencyexchange.model.ExchangeProvider;
import com.serg.currencyexchange.model.ExchangeRate;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface ExchangeRateMapper {

    DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    ExchangeRateRequestDto mapToExchangeRateRequestDto(RatesApiResponseDto dto);

    default ExchangeRateRequestDto mapToExchangeRateRequestDto(RatesApiResponseDto dto, LocalDateTime date) {
        ExchangeRateRequestDto exchangeRateRequestDto = mapToExchangeRateRequestDto(dto);
        exchangeRateRequestDto.setDate(date.toLocalDate().format(DATE_FORMATTER));
        exchangeRateRequestDto.setTime(date.toLocalTime().format(TIME_FORMATTER));
        exchangeRateRequestDto.setProvider(ExchangeProvider.RATES_API);
        return exchangeRateRequestDto;
    }

    ExchangeRateRequestDto mapToExchangeRateRequestDto(ExchangeRateApiResponseDto dto);

    default ExchangeRateRequestDto mapToExchangeRateRequestDto(ExchangeRateApiResponseDto dto, LocalDateTime date) {
        ExchangeRateRequestDto exchangeRateRequestDto = mapToExchangeRateRequestDto(dto);
        exchangeRateRequestDto.setDate(date.toLocalDate().format(DATE_FORMATTER));
        exchangeRateRequestDto.setTime(date.toLocalTime().format(TIME_FORMATTER));
        exchangeRateRequestDto.setProvider(ExchangeProvider.EXCHANGE_RATE_API);
        return exchangeRateRequestDto;
    }

    ExchangeRate mapToExchangeRate(ExchangeRateRequestDto dto);

    ExchangeRateResponseDto mapToExchangeRateResponseDto(ExchangeRate exchangeRate);
}
