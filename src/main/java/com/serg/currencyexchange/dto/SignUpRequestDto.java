package com.serg.currencyexchange.dto;

import lombok.Data;

@Data
public class SignUpRequestDto {

    private String username;
    private String password;
    private String name;
}
