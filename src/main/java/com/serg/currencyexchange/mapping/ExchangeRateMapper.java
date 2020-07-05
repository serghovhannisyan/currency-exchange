package com.serg.currencyexchange.mapping;

import com.serg.currencyexchange.batch.dto.ExchangeRateApiResponseDto;
import com.serg.currencyexchange.batch.dto.RatesApiResponseDto;
import com.serg.currencyexchange.dto.ExchangeRateRequestDto;
import com.serg.currencyexchange.dto.ExchangeRateResponseDto;
import com.serg.currencyexchange.model.ExchangeRate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExchangeRateMapper {

    ExchangeRateRequestDto mapToExchangeRateRequestDto(RatesApiResponseDto dto);

    ExchangeRateRequestDto mapToExchangeRateRequestDto(ExchangeRateApiResponseDto dto);

    ExchangeRate mapToExchangeRate(ExchangeRateRequestDto dto);

    ExchangeRateResponseDto mapToExchangeRateResponseDto(ExchangeRate exchangeRate);
}
