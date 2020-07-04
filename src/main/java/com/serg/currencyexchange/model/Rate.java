package com.serg.currencyexchange.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Rate {

    private String name;
    private BigDecimal value;
}
