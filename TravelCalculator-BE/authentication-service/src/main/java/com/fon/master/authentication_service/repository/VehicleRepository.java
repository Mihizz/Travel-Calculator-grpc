package com.fon.master.authentication_service.repository;

import com.fon.master.authentication_service.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle,Long> {
    List<Vehicle> findByAccountId(long accountId);
}
