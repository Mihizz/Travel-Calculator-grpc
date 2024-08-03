package com.fon.master.pricing_service.payload;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FuelDto {
    private Long id;
    private double priceInDinars;
    private long fuelTypeId;
    private String fuelTypeName;
    private String fuelUnit;
    private long countryId;
    private String countryName;
}
