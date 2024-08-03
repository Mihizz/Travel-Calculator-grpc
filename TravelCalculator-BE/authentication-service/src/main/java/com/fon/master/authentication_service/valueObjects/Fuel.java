package com.fon.master.authentication_service.valueObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fuel {
    private Long id;
    private double price;
    private Long fuelTypeId;
    private Long countryId;
}
