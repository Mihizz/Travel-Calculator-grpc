package com.fon.master.pricing_service.repository;

import com.fon.master.pricing_service.model.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuelTypeRepository extends JpaRepository<FuelType, Long> {
    //FIND FUEL TYPE BY TAG
    Optional<FuelType> findFuelTypeByFuelTagsContainingIgnoreCase(String tag);
}
