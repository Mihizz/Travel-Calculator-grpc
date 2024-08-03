package com.fon.master.authentication_service.service;

import com.fon.master.authentication_service.payload.VehicleTypeDto;

import java.util.List;

public interface VehicleTypeService {

    //CREATE (ONLY FOR BACK-END)
    VehicleTypeDto createVehicleType(VehicleTypeDto vehicleTypeDto);

    //GET ALL
    List<VehicleTypeDto> getAllVehicleTypes();

    //GET BY ID
    VehicleTypeDto getVehicleTypeById(long vehicleTypeId);

}
