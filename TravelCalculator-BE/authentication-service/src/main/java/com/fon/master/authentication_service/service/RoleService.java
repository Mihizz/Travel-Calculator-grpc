package com.fon.master.authentication_service.service;

import com.fon.master.authentication_service.payload.RoleDto;

import java.util.List;

public interface RoleService {
    RoleDto createRole(RoleDto roleDto);

    // GET ALL
    List<RoleDto> getAllRoles();

    // GET BY ID
    RoleDto getRoleById(long id);
}
