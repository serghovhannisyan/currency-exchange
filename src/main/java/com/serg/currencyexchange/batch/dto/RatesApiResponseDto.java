package com.serg.currencyexchange.batch.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class RatesApiResponseDto {

    private String base;
    private Map<String, BigDecimal> rates;
}
