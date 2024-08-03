package com.fon.master.authentication_service.repository;

import com.fon.master.authentication_service.model.Account;
import com.fon.master.authentication_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT r FROM Account a JOIN a.roles r WHERE a.id = :accountId")
    Set<Role> findRoleNamesByAccountId(Long accountId);
}
