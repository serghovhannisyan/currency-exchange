package com.serg.currencyexchange.service.exchangerate;

import com.serg.currencyexchange.dto.ExchangeRateRequestDto;
import com.serg.currencyexchange.dto.ExchangeRateResponseDto;
import com.serg.currencyexchange.exception.BadRequestException;
import com.serg.currencyexchange.mapping.ExchangeRateMapper;
import com.serg.currencyexchange.model.ExchangeProvider;
import com.serg.currencyexchange.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<ExchangeRateResponseDto> getAllByDateAndTime(String date, String time) {
        return exchangeRateRepository.findAllByDateAndTime(date, time)
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
    public Flux<ExchangeRateResponseDto> getLatestByProvider(String provider) {
        return Mono.just(provider)
                .map(ExchangeProvider::valueOf)
                .flatMapMany(exchangeRateRepository::findAllByLatestDateAndProvider)
                .map(exchangeRateMapper::mapToExchangeRateResponseDto)
                .onErrorMap(throwable -> new BadRequestException());
    }

    @Transactional
    @Override
    public Mono<ExchangeRateResponseDto> add(ExchangeRateRequestDto dto) {
        return exchangeRateRepository.save(exchangeRateMapper.mapToExchangeRate(dto))
                .map(exchangeRateMapper::mapToExchangeRateResponseDto);
    }
}
