package com.fon.master.authentication_service.service;

import com.fon.master.authentication_service.payload.VehicleDto;

import java.util.List;

public interface VehicleService {
    //CREATE
    VehicleDto createVehicle(long accountId, VehicleDto vehicleDto);

    //GET BY ID
    VehicleDto getVehicleById(long accountId, long vehicleId);

    //GET BY ACCOUNT ID
    List<VehicleDto> getVehicleByAccountId(long accountId);

    //UPDATE
    VehicleDto updateVehicle(long accountId, long vehicleId, VehicleDto vehicleDto);

    //UPDATE VEHICLE COUTNRY
    VehicleDto updateVehicleCountry(long accountId, long vehicleId, long countryId);

    //DELETE
    void deleteVehicle(long accountId, long vehicleId);
}
