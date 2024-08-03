package com.fon.master.pricing_service.controller;

import com.fon.master.pricing_service.payload.CurrencyDto;
import com.fon.master.pricing_service.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    private CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping
    public ResponseEntity<CurrencyDto> createCurrency(@RequestBody CurrencyDto currencyDto) {
        return new ResponseEntity<>(currencyService.createCurrency(currencyDto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<CurrencyDto> getAllCurrencies() {
        return currencyService.getAllCurrencies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyDto> getCurrencyById(@PathVariable(value = "id") long id) {
        CurrencyDto currencyDto = currencyService.getCurrencyById(id);
        return new ResponseEntity<>(currencyDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrencyDto> updateCurrency(@PathVariable(value = "id") long id,
                                                      @RequestBody CurrencyDto currencyDto) {
        CurrencyDto updatedCurrency = currencyService.updateCurrency(id);
        return new ResponseEntity<>(updatedCurrency, HttpStatus.OK);
    }

}
