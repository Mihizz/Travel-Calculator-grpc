package com.fon.master.pricing_service.service;

import com.fon.master.pricing_service.payload.FuelTypeDto;

import java.util.List;

public interface FuelTypeService {

    // CREATE
    FuelTypeDto createFuelType(FuelTypeDto fuelTypeDto);

    // GET ALL
    List<FuelTypeDto> getAllFuelTypes();

    // GET BY ID
    FuelTypeDto getFuelTypeById(long id);
}
