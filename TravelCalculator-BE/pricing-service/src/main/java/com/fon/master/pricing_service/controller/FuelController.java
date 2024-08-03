package com.fon.master.pricing_service.controller;

import com.fon.master.pricing_service.payload.FuelDto;
import com.fon.master.pricing_service.service.FuelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuels")
public class FuelController {

    private final FuelService fuelService;

    public FuelController(FuelService fuelService) {
        this.fuelService = fuelService;
    }

    @PostMapping
    public ResponseEntity<FuelDto> createFuel(@RequestBody FuelDto fuelDto) {
        return new ResponseEntity<>(fuelService.createFuel(fuelDto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<FuelDto> getAllFuels() {
        return fuelService.getAllFuels();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelDto> getFuelById(@PathVariable(value = "id") long id) {
        FuelDto fuelDto = fuelService.getFuelById(id);
        return new ResponseEntity<>(fuelDto, HttpStatus.OK);
    }

    @GetMapping("/country/{id}")
    public List<FuelDto> getFuelByCountryId(@PathVariable(value = "id") long countryId) {
        return fuelService.getAllFuelsByCountryId(countryId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuelDto> updateFuel(@PathVariable(value = "id") long id) {
        FuelDto updatedFuel = fuelService.updateFuel(id);
        return new ResponseEntity<>(updatedFuel, HttpStatus.OK);
    }

    @GetMapping("/params")
    public ResponseEntity<FuelDto> getFuelByCountryIdAndFuelTypeId(@RequestParam Long countryId, @RequestParam Long fuelTypeId) {
        FuelDto fuelDto = fuelService.getuFuelByCountryIdAndFuelTypeId(countryId, fuelTypeId);
        return new ResponseEntity<>(fuelDto, HttpStatus.OK);
    }
}
