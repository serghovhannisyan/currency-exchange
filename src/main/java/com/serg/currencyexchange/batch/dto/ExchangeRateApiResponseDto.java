package com.serg.currencyexchange.batch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ExchangeRateApiResponseDto {

    @JsonProperty("base_code")
    private String base;
    @JsonProperty("conversion_rates")
    private Map<String, BigDecimal> rates;
}
