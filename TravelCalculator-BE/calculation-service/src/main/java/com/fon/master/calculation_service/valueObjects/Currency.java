package com.fon.master.calculation_service.valueObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Currency {
    private Long id;
    private String currencyName;
    private double exchangeRate;
    private String symbol;
}
