package com.fon.master.pricing_service.payload;

import lombok.Data;

@Data
public class CurrencyDto {
    private Long id;
    private String currencyName;
    private String symbol;
    private double exchangeRate;
    private String updatedAt;
}
