package com.serg.currencyexchange.mapping;

import com.serg.currencyexchange.dto.CurrencyRequestDto;
import com.serg.currencyexchange.dto.CurrencyResponseDto;
import com.serg.currencyexchange.model.Currency;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyResponseDto mapToResponseDto(Currency currency);

    Currency mapToCurrency(CurrencyRequestDto dto);
}
