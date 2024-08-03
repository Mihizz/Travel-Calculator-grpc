package com.fon.master.pricing_service.service.impl;

import com.fon.master.pricing_service.exception.ResourceNotFoundException;
import com.fon.master.pricing_service.model.Currency;
import com.fon.master.pricing_service.payload.CurrencyDto;
import com.fon.master.pricing_service.repository.CurrencyRepository;
import com.fon.master.pricing_service.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public CurrencyDto createCurrency(CurrencyDto currencyDto) {
        Currency currency = mapToEntity(currencyDto);
        Currency savedCurrency = currencyRepository.save(currency);
        return mapToDTO(savedCurrency);
    }

    @Override
    public List<CurrencyDto> getAllCurrencies() {
        List<Currency> currencies = currencyRepository.findAll();
        return currencies.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CurrencyDto getCurrencyById(long id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currency", "id", id));

        LocalDate updatedAtDate = currency.getUpdatedAt().toLocalDate();
        LocalDate today = LocalDate.now();

        if(!updatedAtDate.equals(today)){
            updateCurrency(id);

            currency = currencyRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Currency", "id", id));
        }

        return mapToDTO(currency);
    }

    @Override
    public CurrencyDto updateCurrency(long id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currency", "id", id));

        Selenium selenium = new Selenium(currencyRepository);

        currency.setExchangeRate(selenium.exchangeRateForCurrencies(currency.getSymbol()));
        currency.setUpdatedAt(LocalDateTime.now());

        Currency updatedCurrency = currencyRepository.save(currency);
        return mapToDTO(updatedCurrency);
    }


    // Convert Entity to DTO (send to front-end)
    private CurrencyDto mapToDTO(Currency currency) {
        CurrencyDto currencyDto = new CurrencyDto();

        currencyDto.setId(currency.getId());
        currencyDto.setCurrencyName(currency.getCurrencyName());
        currencyDto.setSymbol(currency.getSymbol());
        currencyDto.setExchangeRate(currency.getExchangeRate());

        currencyDto.setUpdatedAt(String.valueOf(currency.getUpdatedAt()));

        return currencyDto;
    }

    // Convert DTO to Entity (get to front-end)
    private Currency mapToEntity(CurrencyDto currencyDto) {
        Currency currency = new Currency();

        currency.setId(currencyDto.getId());
        currency.setCurrencyName(currencyDto.getCurrencyName());
        currency.setSymbol(currencyDto.getSymbol());
        currency.setExchangeRate(currencyDto.getExchangeRate());

        return currency;
    }
}
