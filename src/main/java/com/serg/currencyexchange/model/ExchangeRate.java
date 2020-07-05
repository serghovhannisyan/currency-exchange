package com.serg.currencyexchange.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Document
public class ExchangeRate {

    @Id
    private String id;

    @Indexed
    private String base;

    private Map<String, BigDecimal> rates;
    private String date;
    private String time;

    @Indexed
    private ExchangeProvider provider;
}
