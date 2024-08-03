package com.fon.master.pricing_service.service;

import com.fon.master.pricing_service.payload.CurrencyDto;

import java.util.List;

public interface CurrencyService {

    // CREATE
    CurrencyDto createCurrency(CurrencyDto currencyDto);

    // GET ALL
    List<CurrencyDto> getAllCurrencies();

    // GET BY ID
    CurrencyDto getCurrencyById(long id);

    // UPDATE
    CurrencyDto updateCurrency(long id);
}
