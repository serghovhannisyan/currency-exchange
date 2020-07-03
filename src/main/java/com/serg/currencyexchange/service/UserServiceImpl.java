package com.serg.currencyexchange.service;

import com.serg.currencyexchange.dto.UserDto;
import com.serg.currencyexchange.exception.NotFoundException;
import com.serg.currencyexchange.mapping.UserMapper;
import com.serg.currencyexchange.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<UserDto> getAllUsers() {
        return userRepository.findAll().map(userMapper::mapUserToUserDto);
    }

    @Transactional
    @Override
    public Mono<UserDto> deleteUser(String id) {
        return userRepository.findById(id)
                .flatMap(existingUser -> userRepository.delete(existingUser)
                        .thenReturn(existingUser)
                        .map(userMapper::mapUserToUserDto))
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }
}
