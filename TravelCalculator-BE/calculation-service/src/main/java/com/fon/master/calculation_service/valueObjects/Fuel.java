package com.fon.master.calculation_service.valueObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fuel {
    private Long id;
    private Long fuelTypeId;
    private String fuelName;
    private String fuelUnit;
    private double priceInDinars;
}
