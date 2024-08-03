package com.fon.master.pricing_service.payload;

import lombok.Data;

@Data
public class CountryDto {
    private Long id;
    private String countryName;
    private String countryNameEng;
    private Long baseCurrencyId;
}
