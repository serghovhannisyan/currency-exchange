package com.serg.currencyexchange.service.auth;

import com.serg.currencyexchange.dto.SignInRequestDto;
import com.serg.currencyexchange.dto.SignInResponseDto;
import com.serg.currencyexchange.dto.SignUpRequestDto;
import com.serg.currencyexchange.dto.SignUpResponseDto;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<SignInResponseDto> signIn(SignInRequestDto signInRequestDto);

    Mono<SignUpResponseDto> signUp(SignUpRequestDto signUpRequestDto);
}
