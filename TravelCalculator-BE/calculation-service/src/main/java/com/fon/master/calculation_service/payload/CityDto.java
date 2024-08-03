package com.fon.master.calculation_service.payload;

import lombok.Data;

@Data
public class CityDto {
    private Long id;
    private String cityName;
    private Long countryId;
    private String googleMapsCode;
}
