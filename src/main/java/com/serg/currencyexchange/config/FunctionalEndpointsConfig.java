package com.serg.currencyexchange.config;

import com.serg.currencyexchange.handler.AuthHandler;
import com.serg.currencyexchange.handler.CurrencyHandler;
import com.serg.currencyexchange.handler.ExchangeRateHandler;
import com.serg.currencyexchange.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
@EnableWebFlux
public class FunctionalEndpointsConfig {

    @Bean
    public RouterFunction<ServerResponse> authRoute(AuthHandler handler) {
        return RouterFunctions
                .route(POST("/api/v1/auth/signin").and(accept(APPLICATION_JSON)), handler::signIn)
                .andRoute(POST("/api/v1/auth/signup").and(accept(APPLICATION_JSON)), handler::signUp);
    }

    @Bean
    public RouterFunction<ServerResponse> userRoute(UserHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/users").and(accept(APPLICATION_JSON)), handler::getAllUsers)
                .andRoute(DELETE("/api/v1/users/{id}").and(accept(APPLICATION_JSON)), handler::deleteUser);
    }

    @Bean
    public RouterFunction<ServerResponse> currencyRoute(CurrencyHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/currencies").and(accept(APPLICATION_JSON)), handler::getAll)
                .andRoute(POST("/api/v1/currencies").and(accept(APPLICATION_JSON)), handler::add)
                .andRoute(PUT("/api/v1/currencies/{id}").and(accept(APPLICATION_JSON)), handler::update)
                .andRoute(DELETE("/api/v1/currencies/{id}").and(accept(APPLICATION_JSON)), handler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> exchangeRateRoute(ExchangeRateHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/exchange-rates").and(accept(APPLICATION_JSON)), handler::getAll)
                .andRoute(GET("/api/v1/exchange-rates/latest").and(accept(APPLICATION_JSON)), handler::getLatest);
    }
}
