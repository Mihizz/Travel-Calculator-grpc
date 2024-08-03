package com.fon.master.calculation_service.valueObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripResponseTemplateVO {
    private Account account;
    private Currency currency;
    private Vehicle vehicle;
    private Fuel fuel;
    private Country country;
}
