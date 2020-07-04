package com.serg.currencyexchange.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Document
public class ExchangeRate {

    @Id
    private String id;
    private List<Rate> rates;
    private LocalDate date;
    private ExchangeProvider provider;
}
