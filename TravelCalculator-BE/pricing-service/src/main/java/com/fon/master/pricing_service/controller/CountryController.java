package com.fon.master.pricing_service.controller;

import com.fon.master.pricing_service.payload.CountryDto;
import com.fon.master.pricing_service.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping
    public ResponseEntity<CountryDto> createCountry(@RequestBody CountryDto countryDto) {
        var response = countryService.createCountry(countryDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public List<CountryDto> getAllCountries() {
        long startTime = System.currentTimeMillis();
        var response = countryService.getAllCountries();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
        return countryService.getAllCountries();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDto> getCountryById(@PathVariable(value = "id") long id) {
        CountryDto countryDto = countryService.getCountryById(id);
        return new ResponseEntity<>(countryDto, HttpStatus.OK);
    }
}
