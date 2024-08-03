package com.fon.master.authentication_service.payload;

import com.fon.master.authentication_service.model.VehicleType;
import lombok.Data;

@Data
public class VehicleDto {
    private Long id;
    private Long vehicleTypeId;
    private Long fuelId;
    private String manufacturer;
    private String model;
    private double consumptionRate;
    private int seats;
}
