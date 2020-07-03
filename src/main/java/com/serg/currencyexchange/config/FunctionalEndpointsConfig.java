package com.serg.currencyexchange.config;

import com.serg.currencyexchange.handler.AuthHandler;
import com.serg.currencyexchange.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
@EnableWebFlux
public class FunctionalEndpointsConfig {

    @Bean
    public RouterFunction<ServerResponse> authRoute(AuthHandler authHandler) {
        return RouterFunctions
                .route(POST("/api/v1/auth/signin").and(accept(MediaType.APPLICATION_JSON)), authHandler::signIn)
                .andRoute(POST("/api/v1/auth/signup").and(accept(MediaType.APPLICATION_JSON)), authHandler::signUp);
    }

    @Bean
    public RouterFunction<ServerResponse> userRoute(UserHandler userHandler) {
        return RouterFunctions
                .route(GET("/api/v1/users").and(accept(MediaType.APPLICATION_JSON)), userHandler::getAllUsers)
                .andRoute(DELETE("/api/v1/users/{id}").and(accept(MediaType.APPLICATION_JSON)), userHandler::deleteUser);
    }
}
