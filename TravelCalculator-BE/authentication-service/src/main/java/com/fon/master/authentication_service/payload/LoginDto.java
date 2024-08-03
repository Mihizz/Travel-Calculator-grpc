package com.fon.master.authentication_service.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {
    @NotNull
    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotNull
    @NotBlank(message = "Password cannot be blank.")
    private String password;
}
