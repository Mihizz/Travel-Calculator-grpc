package com.fon.master.pricing_service.repository;

import com.fon.master.pricing_service.model.Country;
import com.fon.master.pricing_service.model.Fuel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FuelRepository extends JpaRepository<Fuel, Long> {
    List<Fuel> findByCountryId(long countryId);
    Fuel findByCountryIdAndFuelTypeId(long countryId, long fuelTypeId);
}
