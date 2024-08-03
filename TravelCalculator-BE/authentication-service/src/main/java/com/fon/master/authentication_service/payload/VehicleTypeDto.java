package com.fon.master.authentication_service.payload;

import lombok.Data;

@Data
public class VehicleTypeDto {
    private Long id;
    private String vehicleType;
    private int seats;
}
