package com.fon.master.authentication_service.controller;

import com.fon.master.authentication_service.payload.RoleDto;
import com.fon.master.authentication_service.payload.VehicleTypeDto;
import com.fon.master.authentication_service.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        return new ResponseEntity<>(roleService.createRole(roleDto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<RoleDto> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable(value = "id") long id) {
        RoleDto roleDto = roleService.getRoleById(id);
        return new ResponseEntity<>(roleDto, HttpStatus.OK);
    }
}
