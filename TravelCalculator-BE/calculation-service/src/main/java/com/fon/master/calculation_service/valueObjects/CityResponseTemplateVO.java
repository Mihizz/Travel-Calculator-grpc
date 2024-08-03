package com.fon.master.calculation_service.valueObjects;

import com.fon.master.calculation_service.model.City;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityResponseTemplateVO {
    private City city;
    private Country country;
}
