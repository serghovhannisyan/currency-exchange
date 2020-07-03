package com.serg.currencyexchange.service;

import com.serg.currencyexchange.dto.SignInRequestDto;
import com.serg.currencyexchange.dto.SignInResponseDto;
import com.serg.currencyexchange.dto.SignUpRequestDto;
import com.serg.currencyexchange.dto.SignUpResponseDto;
import com.serg.currencyexchange.exception.AlreadyExistsException;
import com.serg.currencyexchange.exception.BadCredentialsException;
import com.serg.currencyexchange.exception.NotFoundException;
import com.serg.currencyexchange.mapping.UserMapper;
import com.serg.currencyexchange.model.Role;
import com.serg.currencyexchange.model.User;
import com.serg.currencyexchange.repository.UserRepository;
import com.serg.currencyexchange.security.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${app.jwt.token-prefix}")
    private String jwtTokenPrefix;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    public AuthServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
    }

    @Transactional
    @Override
    public Mono<SignUpResponseDto> signUp(SignUpRequestDto signUpRequestDto) {
        return userRepository.findByUsername(signUpRequestDto.getUsername())
                .defaultIfEmpty(mapToUser(signUpRequestDto))
                .filter(user -> Objects.isNull(user.getId()))
                .flatMap(userRepository::save)
                .map(userMapper::mapUserToSignUpResponseDto)
                .switchIfEmpty(Mono.error(new AlreadyExistsException()));
    }

    @Transactional
    @Override
    public Mono<SignInResponseDto> signIn(SignInRequestDto signInRequestDto) {
        return userRepository.findByUsername(signInRequestDto.getUsername())
                .flatMap(existingUser -> isValidUser(signInRequestDto, existingUser) ?
                        Mono.just(new SignInResponseDto(jwtUtils.generateToken(existingUser), jwtTokenPrefix)) :
                        Mono.error(new BadCredentialsException()))
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    private boolean isValidUser(SignInRequestDto signInRequestDto, User existingUser) {
        return passwordEncoder.matches(signInRequestDto.getPassword(), existingUser.getPassword());
    }

    private User mapToUser(SignUpRequestDto dto) {
        User user = userMapper.mapSignUpRequestDtoToUser(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Collections.singleton(new Role("USER", "Default role")));
        return user;
    }
}