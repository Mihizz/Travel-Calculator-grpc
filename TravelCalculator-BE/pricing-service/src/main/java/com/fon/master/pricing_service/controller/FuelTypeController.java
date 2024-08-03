package com.fon.master.pricing_service.controller;

import com.fon.master.pricing_service.payload.FuelTypeDto;
import com.fon.master.pricing_service.service.FuelTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuel-types")
public class FuelTypeController {
    private FuelTypeService fuelTypeService;

    public FuelTypeController(FuelTypeService fuelTypeService) {
        this.fuelTypeService = fuelTypeService;
    }

    @PostMapping
    public ResponseEntity<FuelTypeDto> createFuelType(@RequestBody FuelTypeDto fuelTypeDto) {
        return new ResponseEntity<>(fuelTypeService.createFuelType(fuelTypeDto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<FuelTypeDto> getAllFuelTypes() {
        return fuelTypeService.getAllFuelTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelTypeDto> getFuelTypeById(@PathVariable(value = "id") long id) {
        FuelTypeDto fuelTypeDto = fuelTypeService.getFuelTypeById(id);
        return new ResponseEntity<>(fuelTypeDto, HttpStatus.OK);
    }
}
