package com.fon.master.authentication_service.payload;

import com.fon.master.authentication_service.model.Role;
import lombok.Data;

import java.util.Set;

@Data
public class AccountDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private long CountryId;
    private String CountryName;
    private long CurrencyId;
    private String CurrencyName;
    private Set<Role> roles;
}
