package com.fon.master.pricing_service.service;

import com.fon.master.pricing_service.payload.FuelDto;

import java.util.List;

public interface FuelService {
    // CREATE
    FuelDto createFuel(FuelDto fuelDto);

    // GET ALL
    List<FuelDto> getAllFuels();

    // GET ALL BY COUNTRY
    List<FuelDto> getAllFuelsByCountryId(long countryId);

    // GET BY ID
    FuelDto getFuelById(long id);

    // GET ONLY FUEL BY ID
    //FuelDto getOnlyFuelById(long id);

    // UPDATE
    FuelDto updateFuel(long id);

    FuelDto getuFuelByCountryIdAndFuelTypeId(long countryId, long fuelTypeId);
}
