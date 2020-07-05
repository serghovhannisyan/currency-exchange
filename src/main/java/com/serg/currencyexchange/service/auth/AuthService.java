package com.serg.currencyexchange.service.auth;

import com.serg.currencyexchange.dto.SignInRequestDto;
import com.serg.currencyexchange.dto.SignInResponseDto;
import com.serg.currencyexchange.dto.SignUpRequestDto;
import com.serg.currencyexchange.dto.SignUpResponseDto;
import reactor.core.publisher.Mono;

public interface AuthService {

    /**
     * Authenticates user based on credentials
     *
     * @param signInRequestDto object containing fields for username and password
     * @return object containing token and token type {@link SignInResponseDto}
     * @throws com.serg.currencyexchange.exception.BadCredentialsException in case of incorrect credentials
     * @throws com.serg.currencyexchange.exception.NotFoundException in case of username is not found
     */
    Mono<SignInResponseDto> signIn(SignInRequestDto signInRequestDto);

    /**
     * Registers user with role USER
     *
     * @param signUpRequestDto object containing necessary fields for user registration
     * @return persisted user {@link SignUpResponseDto}
     * @throws com.serg.currencyexchange.exception.AlreadyExistsException in case of duplicate username
     */
    Mono<SignUpResponseDto> signUp(SignUpRequestDto signUpRequestDto);
}
