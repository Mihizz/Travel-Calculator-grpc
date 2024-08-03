package com.fon.master.authentication_service.service.impl;

import com.fon.master.authentication_service.exception.ResourceNotFoundException;
import com.fon.master.authentication_service.model.VehicleType;
import com.fon.master.authentication_service.payload.VehicleTypeDto;
import com.fon.master.authentication_service.repository.VehicleTypeRepository;
import com.fon.master.authentication_service.service.VehicleTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleTypeServiceImpl implements VehicleTypeService {

    private VehicleTypeRepository vehicleTypeRepository;

    public VehicleTypeServiceImpl(VehicleTypeRepository vehicleTypeRepository) {
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    @Override
    public VehicleTypeDto createVehicleType(VehicleTypeDto vehicleTypeDto) {
        VehicleType vehicleType = mapToEntity(vehicleTypeDto);

        VehicleType vehicleType1 = vehicleTypeRepository.save(vehicleType);

        return mapToDTO(vehicleType1);
    }

    @Override
    public List<VehicleTypeDto> getAllVehicleTypes() {
        List<VehicleType> vehicleTypes = vehicleTypeRepository.findAll();
        return vehicleTypes.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleTypeDto getVehicleTypeById(long vehicleTypeId) {
        VehicleType vehicleType = vehicleTypeRepository.findById(vehicleTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", vehicleTypeId));
        return mapToDTO(vehicleType);
    }

    //covert entity to DTO
    private VehicleTypeDto mapToDTO(VehicleType vehicleType){
        VehicleTypeDto vehicleTypeDto = new VehicleTypeDto();

        vehicleTypeDto.setId(vehicleType.getId());
        vehicleTypeDto.setVehicleType(vehicleType.getVehicleType());
        vehicleTypeDto.setSeats(vehicleType.getSeats());

        return vehicleTypeDto;
    }

    //covert DTO to entity
    private VehicleType mapToEntity(VehicleTypeDto vehicleTypeDto){
        VehicleType vehicleType = new VehicleType();

        vehicleType.setId(vehicleTypeDto.getId());
        vehicleType.setVehicleType(vehicleTypeDto.getVehicleType());
        vehicleType.setSeats(vehicleTypeDto.getSeats());

        return vehicleType;

    }
}
