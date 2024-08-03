package com.fon.master.calculation_service.valueObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Account {
    private Long id;
    private String firstName;
    private String email;
    private String password;
    private Long countryId;
    private Long currencyId;
}
