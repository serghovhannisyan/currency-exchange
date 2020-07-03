package com.serg.currencyexchange.handler;

import com.serg.currencyexchange.dto.UserDto;
import com.serg.currencyexchange.exception.NotFoundException;
import com.serg.currencyexchange.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UserHandler {

    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        Flux<UserDto> allUsers = userService.getAllUsers();
        return ServerResponse.ok().body(allUsers, UserDto.class);
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        return userService.deleteUser(request.pathVariable("id"))
                .flatMap(userDto -> ServerResponse.noContent().build())
                .onErrorResume(UserHandler::mapToServerResponse);
    }

    private static Mono<? extends ServerResponse> mapToServerResponse(Throwable ex) {
        if (ex instanceof NotFoundException) {
            return ServerResponse.notFound().build();
        }

        log.error("Unhandled error: {}", ex.getMessage(), ex);
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
