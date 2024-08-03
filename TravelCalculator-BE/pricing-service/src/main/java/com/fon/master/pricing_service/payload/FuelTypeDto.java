package com.fon.master.pricing_service.payload;

import lombok.Data;

@Data
public class FuelTypeDto {
    private Long id;
    private String fuelName;
    private String fuelTags;
    private String fuelUnit;
}
