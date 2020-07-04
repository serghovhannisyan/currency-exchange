package com.serg.currencyexchange.dto;

import lombok.Data;

@Data
public class CurrencyResponseDto {

    private String id;
    private String name;
    private boolean enabled;
}
