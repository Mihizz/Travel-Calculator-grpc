package com.fon.master.pricing_service.service.impl;

import com.fon.master.pricing_service.exception.ResourceNotFoundException;
import com.fon.master.pricing_service.model.Country;
import com.fon.master.pricing_service.model.Currency;
import com.fon.master.pricing_service.model.Fuel;
import com.fon.master.pricing_service.model.FuelType;
import com.fon.master.pricing_service.payload.CurrencyDto;
import com.fon.master.pricing_service.payload.FuelDto;
import com.fon.master.pricing_service.repository.CountryRepository;
import com.fon.master.pricing_service.repository.CurrencyRepository;
import com.fon.master.pricing_service.repository.FuelRepository;
import com.fon.master.pricing_service.repository.FuelTypeRepository;
import com.fon.master.pricing_service.service.FuelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuelServiceImpl implements FuelService {

    private FuelRepository fuelRepository;
    private FuelTypeRepository fuelTypeRepository;
    private CountryRepository countryRepository;

    private CurrencyRepository currencyRepository;

    @Autowired
    public FuelServiceImpl(FuelRepository fuelRepository, FuelTypeRepository fuelTypeRepository, CountryRepository countryRepository,CurrencyRepository currencyRepository) {
        this.fuelRepository = fuelRepository;
        this.fuelTypeRepository = fuelTypeRepository;
        this.countryRepository = countryRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public FuelDto createFuel(FuelDto fuelDto) {
        Fuel fuel = mapToEntity(fuelDto);
        Fuel savedFuel = fuelRepository.save(fuel);
        return mapToDTO(savedFuel);
    }

    @Override
    public List<FuelDto> getAllFuels() {
        List<Fuel> fuels = fuelRepository.findAll();
        return fuels.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FuelDto> getAllFuelsByCountryId(long countryId) {
        List<Fuel> fuels = fuelRepository.findByCountryId(countryId);
        return fuels.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FuelDto getFuelById(long id) {
        Fuel fuel = fuelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fuel", "id", id));

        LocalDate updatedAtDate = fuel.getUpdatedAt().toLocalDate();
        LocalDate today = LocalDate.now();

        if(!updatedAtDate.equals(today)){
            updateFuel(id);

            fuel = fuelRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Fuel", "id", id));
        }

        return mapToDTO(fuel);
    }

    @Override
    public FuelDto updateFuel(long id) {
        Fuel fuel = fuelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fuel", "id", id));

        DecimalFormat df = new DecimalFormat("#.00");

        Selenium selenium = new Selenium(fuelTypeRepository,currencyRepository);

        fuel.setPrice(Double.parseDouble(df.format(selenium.fuelPriceFromBenzinko(fuel))));
        fuel.setUpdatedAt(LocalDateTime.now());

        Fuel updatedFuel = fuelRepository.save(fuel);
        return mapToDTO(updatedFuel);
    }

    @Override
    public FuelDto getuFuelByCountryIdAndFuelTypeId(long countryId, long fuelTypeId) {
        Fuel fuel = fuelRepository.findByCountryIdAndFuelTypeId(countryId, fuelTypeId);
        return mapToDTO(fuel);
    }

    // Convert Entity to DTO (send to front-end)
    private FuelDto mapToDTO(Fuel fuel) {
        FuelDto fuelDto = new FuelDto();

        fuelDto.setId(fuel.getId());
        fuelDto.setPriceInDinars(fuel.getPrice());

        fuelDto.setFuelTypeId(fuel.getFuelType().getId());
        fuelDto.setFuelTypeName(fuel.getFuelType().getFuelName());
        fuelDto.setFuelUnit(fuel.getFuelType().getFuelUnit());

        fuelDto.setCountryId(fuel.getCountry().getId());
        fuelDto.setCountryName(fuel.getCountry().getCountryName());

        return fuelDto;
    }

    // Convert DTO to Entity (get to front-end)
    private Fuel mapToEntity(FuelDto fuelDto) {
        Fuel fuel = new Fuel();

        fuel.setId(fuelDto.getId());
        fuel.setPrice(fuelDto.getPriceInDinars());

        FuelType fuelType = fuelTypeRepository.findById(fuelDto.getFuelTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("FuelType", "id", fuelDto.getFuelTypeId()));
        fuel.setFuelType(fuelType);

        Country country = countryRepository.findById(fuelDto.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("Country", "id", fuelDto.getCountryId()));
        fuel.setCountry(country);

        return fuel;
    }
}
