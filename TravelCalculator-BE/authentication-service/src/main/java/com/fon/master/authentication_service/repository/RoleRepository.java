package com.fon.master.authentication_service.repository;

import com.fon.master.authentication_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
