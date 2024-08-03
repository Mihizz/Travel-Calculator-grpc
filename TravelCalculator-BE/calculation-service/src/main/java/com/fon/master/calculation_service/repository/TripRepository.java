package com.fon.master.calculation_service.repository;

import com.fon.master.calculation_service.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {
}
