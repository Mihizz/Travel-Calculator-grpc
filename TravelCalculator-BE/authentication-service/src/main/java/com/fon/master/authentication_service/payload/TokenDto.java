package com.fon.master.authentication_service.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {
    private String token;

    private long expiresIn;
}
