package com.fon.master.pricing_service.service.impl;

import com.fon.master.pricing_service.exception.ResourceNotFoundException;
import com.fon.master.pricing_service.model.Country;
import com.fon.master.pricing_service.model.Currency;
import com.fon.master.pricing_service.payload.CountryDto;
import com.fon.master.pricing_service.repository.CountryRepository;
import com.fon.master.pricing_service.repository.CurrencyRepository;
import com.fon.master.pricing_service.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, CurrencyRepository currencyRepository) {
        this.countryRepository = countryRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public CountryDto createCountry(CountryDto countryDto) {
        Country country = mapToEntity(countryDto);

        //retrieve currency entity by id
        long currencyId = countryDto.getBaseCurrencyId();
        Currency currency = currencyRepository.findById(currencyId).orElseThrow(() -> new ResourceNotFoundException("Currency","id",currencyId));

        country.setBaseCurrency(currency);

        Country savedCountry = countryRepository.save(country);

        return mapToDTO(savedCountry);
    }

    @Override
    public List<CountryDto> getAllCountries() {
        List<Country> countries = countryRepository.findAll();
        return countries.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CountryDto getCountryById(long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country", "id", id));
        return mapToDTO(country);
    }

    // Convert Entity to DTO
    private CountryDto mapToDTO(Country country) {
        CountryDto countryDto = new CountryDto();

        countryDto.setId(country.getId());
        countryDto.setCountryName(country.getCountryName());
        countryDto.setCountryNameEng(country.getCountryNameEng());

        countryDto.setBaseCurrencyId(country.getBaseCurrency().getId());
        //countryDto.setCurrency(currencyRepository.getById(countryDto.getBaseCurrencyId()));

        return countryDto;
    }

    // Convert DTO to Entity
    private Country mapToEntity(CountryDto countryDto) {
        Country country = new Country();

        country.setId(countryDto.getId());
        country.setCountryName(countryDto.getCountryName());
        country.setCountryNameEng(countryDto.getCountryNameEng());

        Currency currency = currencyRepository.findById(countryDto.getBaseCurrencyId())
                .orElseThrow(() -> new ResourceNotFoundException("Currency", "id", countryDto.getBaseCurrencyId()));
        country.setBaseCurrency(currency);

        return country;
    }


}
