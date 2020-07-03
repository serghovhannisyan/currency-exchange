package com.serg.currencyexchange;

import com.serg.currencyexchange.model.Role;
import com.serg.currencyexchange.model.User;
import com.serg.currencyexchange.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
@Slf4j
public class CurrencyExchangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyExchangeApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> userRepository.save(new User(
                null,
                "admin",
                passwordEncoder.encode("password"),
                "Serg",
                new HashSet<>(Arrays.asList(
                        new Role("USER", "Simple role"),
                        new Role("ADMIN", "Privileged role")))))
                .subscribe(user -> log.info("Admin created"));
    }

}
