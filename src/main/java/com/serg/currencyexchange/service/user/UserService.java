package com.serg.currencyexchange.service.user;

import com.serg.currencyexchange.dto.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    /**
     * Retrieve all users
     *
     * @return {@link Flux} emitting all users
     */
    Flux<UserDto> getAllUsers();

    /**
     * Deletes a given user
     *
     * @param id of the user to be deleted
     * @return empty {@link Mono} signaling when operation has completed.
     * @throws com.serg.currencyexchange.exception.NotFoundException in case of given id not found
     */
    Mono<UserDto> deleteUser(String id);
}
