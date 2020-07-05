package com.serg.currencyexchange;

import com.serg.currencyexchange.model.Currency;
import com.serg.currencyexchange.model.Role;
import com.serg.currencyexchange.model.User;
import com.serg.currencyexchange.repository.CurrencyRepository;
import com.serg.currencyexchange.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
@Slf4j
public class CurrencyExchangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyExchangeApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(UserRepository userRepository,
                                  CurrencyRepository currencyRepository,
                                  BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.save(new User(
                    null,
                    "admin",
                    passwordEncoder.encode("password"),
                    "Serg",
                    new HashSet<>(Arrays.asList(
                            new Role("USER", "Simple role"),
                            new Role("ADMIN", "Privileged role")))))
                    .onErrorResume(throwable -> {
                        log.info("Already exist");
                        return Mono.empty();
                    })
                    .subscribe(user -> log.info("Admin created"));

            Flux.just(new Currency(null, "USD", true), new Currency(null, "EUR", true))
                    .flatMap(currencyRepository::save)
                    .onErrorResume(throwable -> {
                        log.info("Already exist");
                        return Mono.empty();
                    })
                    .subscribe(currency -> log.info("Currency created"));
        };
    }

}
