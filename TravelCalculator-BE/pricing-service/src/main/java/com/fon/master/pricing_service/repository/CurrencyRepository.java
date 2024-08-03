package com.fon.master.pricing_service.repository;

import com.fon.master.pricing_service.model.Country;
import com.fon.master.pricing_service.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findByCurrencyName(String currencyName);
}
