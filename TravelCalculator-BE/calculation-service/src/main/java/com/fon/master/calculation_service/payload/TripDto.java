package com.fon.master.calculation_service.payload;

import lombok.Data;

import java.util.List;

@Data
public class TripDto {
    private Long id;
    private double tripDistance;
    private String time;
    private int paytool;
    private Long cityId1;
    private String cityName1;
    private Long cityId2;
    private String cityName2;
    private Double consumptionRate;
    private Double fuelPriceInDinars;
    private Double fullPrice;
    private Long fuelId;
    private String fuelUnit;
    private Long countryId;
    private Long currencyId;
    private String currencySymbol;
    private Double exchangeRate;
    private Long vehicleId;
    private Long accountId;
    private int fuelSpent;
    private int seats;
    private Long executionTime;
}
