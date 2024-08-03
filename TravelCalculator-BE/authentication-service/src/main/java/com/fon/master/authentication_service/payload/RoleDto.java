package com.fon.master.authentication_service.payload;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class RoleDto {
    private Long id;
    private String name;
}
