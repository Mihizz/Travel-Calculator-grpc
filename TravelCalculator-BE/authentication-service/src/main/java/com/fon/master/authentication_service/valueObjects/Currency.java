package com.fon.master.authentication_service.valueObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    private Long id;
    private String currencyName;
    private String symbol;
    private double exchangeRate;
}
