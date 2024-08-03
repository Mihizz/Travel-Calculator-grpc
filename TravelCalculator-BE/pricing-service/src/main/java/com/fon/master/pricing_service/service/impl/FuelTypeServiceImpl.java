package com.fon.master.pricing_service.service.impl;

import com.fon.master.pricing_service.exception.ResourceNotFoundException;
import com.fon.master.pricing_service.model.FuelType;
import com.fon.master.pricing_service.payload.FuelTypeDto;
import com.fon.master.pricing_service.repository.FuelTypeRepository;
import com.fon.master.pricing_service.service.FuelTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuelTypeServiceImpl implements FuelTypeService {

    private FuelTypeRepository fuelTypeRepository;

    @Autowired
    public FuelTypeServiceImpl(FuelTypeRepository fuelTypeRepository) {
        this.fuelTypeRepository = fuelTypeRepository;
    }
    @Override
    public FuelTypeDto createFuelType(FuelTypeDto fuelTypeDto) {
        FuelType fuelType = mapToEntity(fuelTypeDto);
        FuelType savedFuelType = fuelTypeRepository.save(fuelType);
        return mapToDTO(savedFuelType);
    }

    @Override
    public List<FuelTypeDto> getAllFuelTypes() {
        List<FuelType> fuelTypes = fuelTypeRepository.findAll();
        return fuelTypes.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FuelTypeDto getFuelTypeById(long id) {
        FuelType fuelType = fuelTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FuelType", "id", id));
        return mapToDTO(fuelType);
    }

    // Convert Entity to DTO
    private FuelTypeDto mapToDTO(FuelType fuelType) {
        FuelTypeDto fuelTypeDto = new FuelTypeDto();
        fuelTypeDto.setId(fuelType.getId());
        fuelTypeDto.setFuelName(fuelType.getFuelName());
        fuelTypeDto.setFuelTags(fuelType.getFuelTags());
        fuelTypeDto.setFuelUnit(fuelType.getFuelUnit());
        return fuelTypeDto;
    }

    // Convert DTO to Entity
    private FuelType mapToEntity(FuelTypeDto fuelTypeDto) {
        FuelType fuelType = new FuelType();
        fuelType.setId(fuelTypeDto.getId());
        fuelType.setFuelName(fuelTypeDto.getFuelName());
        fuelType.setFuelTags(fuelTypeDto.getFuelTags());
        fuelType.setFuelUnit(fuelTypeDto.getFuelUnit());
        return fuelType;
    }
}
