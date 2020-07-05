package com.serg.currencyexchange.config;

import com.serg.currencyexchange.dto.*;
import com.serg.currencyexchange.handler.AuthHandler;
import com.serg.currencyexchange.handler.CurrencyHandler;
import com.serg.currencyexchange.handler.ExchangeRateHandler;
import com.serg.currencyexchange.handler.UserHandler;
import com.serg.currencyexchange.model.ExchangeProvider;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@OpenAPIDefinition(
        info = @Info(
                title = "Currency Exchange Rates",
                version = "v1",
                description = "API for Exchange Rates",
                contact = @Contact(name = "Sergey", email = "serojhovhannisyan@gmail.com")
        )
)
@Configuration
@EnableWebFlux
public class FunctionalEndpointsConfig {

    @RouterOperations({
            @RouterOperation(path = "/api/v1/auth/signin", method = RequestMethod.POST,
                    operation = @Operation(operationId = "signin", tags = "auth", summary = "Sign in with username and password",
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SignInRequestDto.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SignInResponseDto.class))),
                                    @ApiResponse(responseCode = "404", description = "User not found")
                            })),
            @RouterOperation(path = "/api/v1/auth/signup", method = RequestMethod.POST,
                    operation = @Operation(operationId = "signup", tags = "auth", summary = "Sign up as a user",
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SignUpRequestDto.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SignUpResponseDto.class))),
                                    @ApiResponse(responseCode = "409", description = "User already exist")
                            }))
    })
    @Bean
    public RouterFunction<ServerResponse> authRoute(AuthHandler handler) {
        return RouterFunctions
                .route(POST("/api/v1/auth/signin").and(accept(APPLICATION_JSON)), handler::signIn)
                .andRoute(POST("/api/v1/auth/signup").and(accept(APPLICATION_JSON)), handler::signUp);
    }

    @RouterOperations({
            @RouterOperation(path = "/api/v1/users", method = RequestMethod.GET,
                    operation = @Operation(operationId = "allUsers", tags = "user", summary = "Get all users",
                            responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))))),
            @RouterOperation(path = "/api/v1/users/{id}", method = RequestMethod.DELETE,
                    operation = @Operation(operationId = "deleteUser", tags = "user", summary = "Delete user",
                            parameters = @Parameter(name = "id", in = ParameterIn.PATH, description = "Id of the user"),
                            responses = {
                                    @ApiResponse(responseCode = "204"),
                                    @ApiResponse(responseCode = "404", description = "User with given id not found")
                            }))
    })
    @Bean
    public RouterFunction<ServerResponse> userRoute(UserHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/users").and(accept(APPLICATION_JSON)), handler::getAllUsers)
                .andRoute(DELETE("/api/v1/users/{id}").and(accept(APPLICATION_JSON)), handler::deleteUser);
    }

    @RouterOperations({
            @RouterOperation(path = "/api/v1/currencies", method = RequestMethod.GET,
                    operation = @Operation(operationId = "allCurrencies", tags = "currency", summary = "Get all currencies",
                            responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CurrencyResponseDto.class)))))),
            @RouterOperation(path = "/api/v1/currencies", method = RequestMethod.POST,
                    operation = @Operation(operationId = "createCurrency", tags = "currency", summary = "Create new currency",
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CurrencyRequestDto.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201"),
                                    @ApiResponse(responseCode = "409", description = "Currency already exist")
                            })),
            @RouterOperation(path = "/api/v1/currencies/{id}", method = RequestMethod.PUT,
                    operation = @Operation(operationId = "updateCurrency", tags = "currency", summary = "Update currency",
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
    public RouterFunction<ServerResponse> currencyRoute(CurrencyHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/currencies").and(accept(APPLICATION_JSON)), handler::getAll)
                .andRoute(POST("/api/v1/currencies").and(accept(APPLICATION_JSON)), handler::add)
                .andRoute(PUT("/api/v1/currencies/{id}").and(accept(APPLICATION_JSON)), handler::update)
                .andRoute(DELETE("/api/v1/currencies/{id}").and(accept(APPLICATION_JSON)), handler::delete);
    }

    @RouterOperations({
            @RouterOperation(path = "/api/v1/exchange-rates", method = RequestMethod.GET,
                    operation = @Operation(operationId = "allExchangeRates", tags = "exchange-rate", summary = "Get all exchange rates",
                            parameters = {
                                    @Parameter(name = "date", in = ParameterIn.QUERY, example = "2020-07-05", content = @Content(schema = @Schema(implementation = String.class)), description = "in order to get exchange rates in a given date and time otherwise all list"),
                                    @Parameter(name = "time", in = ParameterIn.QUERY, example = "18:45:05", content = @Content(schema = @Schema(implementation = String.class)), description = "in order to get exchange rates in a given date and time otherwise all list")
                            },
                            responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExchangeRateResponseDto.class)))))),
            @RouterOperation(path = "/api/v1/exchange-rates/latest", method = RequestMethod.GET,
                    operation = @Operation(operationId = "allExchangeRates", tags = "exchange-rate", summary = "Get all exchange rates",
                            parameters = @Parameter(name = "provider", in = ParameterIn.QUERY, content = @Content(schema = @Schema(implementation = ExchangeProvider.class)), description = "in order to get exchange rates based on provider otherwise all list"),
                            responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ExchangeRateResponseDto.class))))))
    })
    @Bean
    public RouterFunction<ServerResponse> exchangeRateRoute(ExchangeRateHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/exchange-rates").and(accept(APPLICATION_JSON)), handler::getAll)
                .andRoute(GET("/api/v1/exchange-rates/latest").and(accept(APPLICATION_JSON)), handler::getLatest);
    }
}
