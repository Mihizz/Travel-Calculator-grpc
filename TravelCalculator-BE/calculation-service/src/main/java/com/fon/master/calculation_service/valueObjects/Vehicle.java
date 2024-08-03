package com.fon.master.calculation_service.valueObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    private Long id;
    private Long fuelId;
    private String manufacturer;
    private String model;
    private double consumptionRate;
    private int seats;
}
