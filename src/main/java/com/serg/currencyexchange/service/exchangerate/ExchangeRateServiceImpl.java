package com.serg.currencyexchange.service.exchangerate;

import com.serg.currencyexchange.dto.ExchangeRateRequestDto;
import com.serg.currencyexchange.dto.ExchangeRateResponseDto;
import com.serg.currencyexchange.mapping.ExchangeRateMapper;
import com.serg.currencyexchange.model.ExchangeProvider;
import com.serg.currencyexchange.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateMapper exchangeRateMapper;

    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository, ExchangeRateMapper exchangeRateMapper) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateMapper = exchangeRateMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<ExchangeRateResponseDto> getAll() {
        return exchangeRateRepository.findAll()
                .map(exchangeRateMapper::mapToExchangeRateResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<ExchangeRateResponseDto> getAllByDate(LocalDateTime date) {
        return exchangeRateRepository.findAllByDate(date)
                .map(exchangeRateMapper::mapToExchangeRateResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<ExchangeRateResponseDto> getAllByDateGreaterThan(LocalDateTime date) {
        return exchangeRateRepository.findAllByDateGreaterThan(date)
                .map(exchangeRateMapper::mapToExchangeRateResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<ExchangeRateResponseDto> getLatest() {
        return exchangeRateRepository.findAllByLatestDate()
                .map(exchangeRateMapper::mapToExchangeRateResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<ExchangeRateResponseDto> getLatestByProvider(ExchangeProvider provider) {
        return exchangeRateRepository.findAllByLatestDateAndProvider(provider)
                .map(exchangeRateMapper::mapToExchangeRateResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<ExchangeRateResponseDto> getLatestByBase(String base) {
        return exchangeRateRepository.findAllByLatestDateAndBase(base)
                .map(exchangeRateMapper::mapToExchangeRateResponseDto);
    }

    @Transactional
    @Override
    public Mono<ExchangeRateResponseDto> add(ExchangeRateRequestDto dto) {
        return exchangeRateRepository.save(exchangeRateMapper.mapToExchangeRate(dto))
                .map(exchangeRateMapper::mapToExchangeRateResponseDto);
    }
}
