package com.fon.master.calculation_service.repository;

import com.fon.master.calculation_service.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
    // Additional query methods can be defined here if needed
}
