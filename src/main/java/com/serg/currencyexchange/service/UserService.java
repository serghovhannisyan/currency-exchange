package com.serg.currencyexchange.service;

import com.serg.currencyexchange.dto.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Flux<UserDto> getAllUsers();

    Mono<UserDto> deleteUser(String id);
}
