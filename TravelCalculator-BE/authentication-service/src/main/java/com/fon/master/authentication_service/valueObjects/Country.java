package com.fon.master.authentication_service.valueObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    private Long id;
    private String countryName;
    private String countryNameEng;
    private Long baseCurrencyId;
}
