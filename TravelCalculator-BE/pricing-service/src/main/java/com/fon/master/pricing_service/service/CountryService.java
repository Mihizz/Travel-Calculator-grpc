package com.fon.master.pricing_service.service;

import com.fon.master.pricing_service.payload.CountryDto;

import java.util.List;

public interface CountryService {
    // CREATE
    CountryDto createCountry(CountryDto countryDto);

    // GET ALL
    List<CountryDto> getAllCountries();

    // GET BY ID
    CountryDto getCountryById(long id);
}
