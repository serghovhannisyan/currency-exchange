package com.serg.currencyexchange.dto;

import com.serg.currencyexchange.model.ExchangeProvider;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ExchangeRateResponseDto {

    private String id;
    private String base;
    private Map<String, BigDecimal> rates;
    private String date;
    private String time;
    private ExchangeProvider provider;
}
