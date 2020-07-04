package com.serg.currencyexchange.service.currency;

import com.serg.currencyexchange.dto.CurrencyRequestDto;
import com.serg.currencyexchange.dto.CurrencyResponseDto;
import com.serg.currencyexchange.exception.AlreadyExistsException;
import com.serg.currencyexchange.exception.NotFoundException;
import com.serg.currencyexchange.mapping.CurrencyMapper;
import com.serg.currencyexchange.repository.CurrencyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Transactional
@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<CurrencyResponseDto> getAll() {
        return currencyRepository.findAll()
                .map(currencyMapper::mapToResponseDto);
    }

    @Override
    public Mono<CurrencyResponseDto> add(CurrencyRequestDto dto) {
        return currencyRepository.findByName(dto.getName())
                .defaultIfEmpty(currencyMapper.mapToCurrency(dto))
                .flatMap(existing -> {
                    if (Objects.isNull(existing.getId())) {
                        return currencyRepository.save(existing).map(currencyMapper::mapToResponseDto);
                    } else {
                        return Mono.error(new AlreadyExistsException());
                    }
                });
    }

    @Override
    public Mono<CurrencyResponseDto> update(String id, CurrencyRequestDto dto) {
        return currencyRepository.findById(id)
                .flatMap(existing -> {
                    existing.setName(dto.getName());
                    existing.setEnabled(dto.isEnabled());
                    return currencyRepository.save(existing).map(currencyMapper::mapToResponseDto);
                })
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Override
    public Mono<CurrencyResponseDto> delete(String id) {
        return currencyRepository.findById(id)
                .flatMap(existing -> currencyRepository.delete(existing)
                        .thenReturn(existing)
                        .map(currencyMapper::mapToResponseDto))
                .switchIfEmpty(Mono.error(new NotFoundException()));
    }
}
