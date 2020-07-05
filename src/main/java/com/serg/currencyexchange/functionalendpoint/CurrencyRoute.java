package com.serg.currencyexchange.functionalendpoint;

import com.serg.currencyexchange.dto.CurrencyRequestDto;
import com.serg.currencyexchange.dto.CurrencyResponseDto;
import com.serg.currencyexchange.handler.CurrencyHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * Functional router for currencies endpoint.
 * Handles all incoming request for /api/v1/currencies and delegates to handler {@link CurrencyHandler}
 *
 * @author Sergey Hovhannisyan
 */
@Configuration
public class CurrencyRoute {

    @RouterOperations({
            @RouterOperation(path = "/api/v1/currencies", method = RequestMethod.GET,
                    operation = @Operation(operationId = "allCurrencies", tags = "currency", summary = "Get all currencies",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CurrencyResponseDto.class)))))),
            @RouterOperation(path = "/api/v1/currencies", method = RequestMethod.POST,
                    operation = @Operation(operationId = "createCurrency", tags = "currency", summary = "Create new currency",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CurrencyRequestDto.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201"),
                                    @ApiResponse(responseCode = "409", description = "Currency already exist")
                            })),
            @RouterOperation(path = "/api/v1/currencies/{id}", method = RequestMethod.PUT,
                    operation = @Operation(operationId = "updateCurrency", tags = "currency", summary = "Update currency",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            parameters = @Parameter(name = "id", in = ParameterIn.PATH, description = "Id of the currency"),
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CurrencyRequestDto.class))),
                            responses = {
                                    @ApiResponse(responseCode = "202", content = @Content(schema = @Schema(implementation = CurrencyResponseDto.class))),
                                    @ApiResponse(responseCode = "404", description = "Currency with given id not found")
                            })),
            @RouterOperation(path = "/api/v1/currencies/{id}", method = RequestMethod.DELETE,
                    operation = @Operation(operationId = "deleteCurrency", tags = "currency", summary = "Delete currencies",
                            parameters = @Parameter(name = "id", in = ParameterIn.PATH, description = "Id of the currency"),
                            responses = {
                                    @ApiResponse(responseCode = "204"),
                                    @ApiResponse(responseCode = "404", description = "Currency with given id not found")
                            }))
    })
    @Bean
    public RouterFunction<ServerResponse> currencyRouter(CurrencyHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/currencies").and(accept(APPLICATION_JSON)), handler::getAll)
                .andRoute(POST("/api/v1/currencies").and(accept(APPLICATION_JSON)), handler::add)
                .andRoute(PUT("/api/v1/currencies/{id}").and(accept(APPLICATION_JSON)), handler::update)
                .andRoute(DELETE("/api/v1/currencies/{id}").and(accept(APPLICATION_JSON)), handler::delete);
    }
}
