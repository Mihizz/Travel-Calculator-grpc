package com.fon.master.authentication_service.service.impl;

import com.fon.master.authentication_service.exception.ResourceNotFoundException;
import com.fon.master.authentication_service.model.Role;
import com.fon.master.authentication_service.payload.RoleDto;
import com.fon.master.authentication_service.repository.RoleRepository;
import com.fon.master.authentication_service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public RoleDto createRole(RoleDto roleDto) {
        Role role = mapToEntity(roleDto);
        Role savedRole = roleRepository.save(role);
        return mapToDTO(savedRole);
    }

    @Override
    public List<RoleDto> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDto getRoleById(long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        return mapToDTO(role);
    }

    // Convert Entity to DTO
    private RoleDto mapToDTO(Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setName(role.getName());
        return roleDto;
    }

    // Convert DTO to Entity
    private Role mapToEntity(RoleDto roleDto) {
        Role role = new Role();
        role.setId(roleDto.getId());
        role.setName(roleDto.getName());
        return role;
    }
}
