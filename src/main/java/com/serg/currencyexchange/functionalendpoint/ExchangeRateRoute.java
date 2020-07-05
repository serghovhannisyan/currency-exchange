package com.serg.currencyexchange.functionalendpoint;

import com.serg.currencyexchange.dto.ExchangeRateResponseDto;
import com.serg.currencyexchange.handler.ExchangeRateHandler;
import com.serg.currencyexchange.model.ExchangeProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * Functional router for exchange rates endpoint.
 * Handles all incoming request for /api/v1/exchange-rates and delegates to handler {@link ExchangeRateHandler}
 *
 * @author Sergey Hovhannisyan
 */
@Configuration
public class ExchangeRateRoute {

    @RouterOperations({
            @RouterOperation(path = "/api/v1/exchange-rates", method = RequestMethod.GET,
                    operation = @Operation(operationId = "allExchangeRates", tags = "exchange-rate", summary = "Get all exchange rates",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            parameters = {
                                    @Parameter(name = "date", in = ParameterIn.QUERY, example = "2020-07-05", content = @Content(schema = @Schema(implementation = String.class)), description = "in order to get exchange rates in a given date and time otherwise all list"),
                                    @Parameter(name = "time", in = ParameterIn.QUERY, example = "18:45:05", content = @Content(schema = @Schema(implementation = String.class)), description = "in order to get exchange rates in a given date and time otherwise all list")
                            },
                            responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExchangeRateResponseDto.class)))))),
            @RouterOperation(path = "/api/v1/exchange-rates/latest", method = RequestMethod.GET,
                    operation = @Operation(operationId = "allExchangeRates", tags = "exchange-rate", summary = "Get all exchange rates",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            parameters = @Parameter(name = "provider", in = ParameterIn.QUERY, content = @Content(schema = @Schema(implementation = ExchangeProvider.class)), description = "in order to get exchange rates based on provider otherwise all list"),
                            responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExchangeRateResponseDto.class))))))
    })
    @Bean
    public RouterFunction<ServerResponse> exchangeRateRouter(ExchangeRateHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/exchange-rates").and(accept(APPLICATION_JSON)), handler::getAll)
                .andRoute(GET("/api/v1/exchange-rates/latest").and(accept(APPLICATION_JSON)), handler::getLatest);
    }
}
