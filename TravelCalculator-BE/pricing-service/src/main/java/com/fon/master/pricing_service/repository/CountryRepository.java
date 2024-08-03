package com.fon.master.pricing_service.repository;

import com.fon.master.pricing_service.model.Country;
import com.fon.master.pricing_service.model.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findCountryByCountryNameEng(String countryEng);
}
