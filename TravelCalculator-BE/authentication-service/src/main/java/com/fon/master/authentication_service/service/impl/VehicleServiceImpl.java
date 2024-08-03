package com.fon.master.authentication_service.service.impl;

import com.fon.master.authentication_service.exception.BlogAPIException;
import com.fon.master.authentication_service.exception.ResourceNotFoundException;
import com.fon.master.authentication_service.model.Account;
import com.fon.master.authentication_service.model.Vehicle;
import com.fon.master.authentication_service.model.VehicleType;
import com.fon.master.authentication_service.payload.VehicleDto;
import com.fon.master.authentication_service.repository.AccountRepository;
import com.fon.master.authentication_service.repository.VehicleRepository;
import com.fon.master.authentication_service.repository.VehicleTypeRepository;
import com.fon.master.authentication_service.service.VehicleService;
import com.fon.master.authentication_service.valueObjects.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class VehicleServiceImpl implements VehicleService {

    VehicleRepository vehicleRepository;
    VehicleTypeRepository vehicleTypeRepository;
    AccountRepository accountRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, VehicleTypeRepository vehicleTypeRepository, AccountRepository accountRepository) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.accountRepository = accountRepository;
    }

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public VehicleDto createVehicle(long accountId, VehicleDto vehicleDto) {
        Vehicle vehicle = mapToEntity(vehicleDto);

        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account","id",accountId));
        vehicle.setAccount(account);

        Vehicle vehicle1 = vehicleRepository.save(vehicle);

        return mapToDTO(vehicle1);
    }

    @Override
    public VehicleDto getVehicleById(long accountId, long vehicleId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account","id",accountId));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", vehicleId));

        Fuel fuel = restTemplate.getForObject("http://pricing-service/fuels/" + vehicle.getFuelId()
                ,Fuel.class);

        if(fuel == null){
            throw new ResourceNotFoundException("Fuel", "id", vehicle.getFuelId());
        }

        if(!(vehicle.getAccount().getId() == (account.getId()))){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Car does not belong to this account");
        }

        return mapToDTO(vehicle);
    }


    @Override
    public List<VehicleDto> getVehicleByAccountId(long accountId) {
        List<Vehicle> vehicles = vehicleRepository.findByAccountId(accountId);

        return vehicles.stream()
                .map(vehicle -> {
                    Fuel fuel = restTemplate.getForObject(
                            "http://pricing-service/fuels/" + vehicle.getFuelId(),
                            Fuel.class);

                    vehicle.setFuelId(fuel.getId());
                    return mapToDTO(vehicle);
                })
                .collect(Collectors.toList());
    }

    @Override
    public VehicleDto updateVehicle(long accountId, long vehicleId, VehicleDto vehicleDto) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account","id",accountId));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", vehicleId));

        if(!(vehicle.getAccount().getId() == (account.getId()))){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Car does not belong to this account");
        }

        mapToEntityUpdate(vehicleDto,vehicle);

        Vehicle vehicle1 = vehicleRepository.save(vehicle);
        return mapToDTO(vehicle1);
    }

    @Override
    public VehicleDto updateVehicleCountry(long accountId, long vehicleId, long countryId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account","id",accountId));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", vehicleId));

        if(!(vehicle.getAccount().getId() == (account.getId()))){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Car does not belong to this account");
        }

        Fuel fuel = restTemplate.getForObject("http://pricing-service/fuels/" + vehicle.getFuelId()
                ,Fuel.class);

        long fuelTypeId = fuel.getFuelTypeId();

        Fuel fuel1 = restTemplate.getForObject("http://pricing-service/fuels/params?countryId=" + countryId + "&fuelTypeId=" + fuelTypeId
                ,Fuel.class);

        vehicle.setFuelId(fuel1.getId());
        Vehicle vehicle1 = vehicleRepository.save(vehicle);

        return mapToDTO(vehicle1);

    }

    @Override
    public void deleteVehicle(long accountId, long vehicleId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account","id",accountId));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", vehicleId));

        if(!(vehicle.getAccount().getId() == (account.getId()))){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Car does not belong to this account");
        }

        vehicleRepository.delete(vehicle);

    }

    private VehicleDto mapToDTO(Vehicle vehicle) {
        VehicleDto vehicleDto = new VehicleDto();

        vehicleDto.setId(vehicle.getId());
        vehicleDto.setManufacturer(vehicle.getManufacturer());
        vehicleDto.setModel(vehicle.getModel());
        vehicleDto.setConsumptionRate(vehicle.getConsumptionRate());

        vehicleDto.setFuelId(vehicle.getFuelId());

        vehicleDto.setVehicleTypeId(vehicle.getVehicleType().getId());
        vehicleDto.setSeats(vehicle.getVehicleType().getSeats());

        return vehicleDto;
    }

    // Convert DTO to Entity
    private Vehicle mapToEntity(VehicleDto vehicleDto) {
        Vehicle vehicle = new Vehicle();

        vehicle.setId(vehicleDto.getId());
        vehicle.setManufacturer(vehicleDto.getManufacturer());
        vehicle.setModel(vehicleDto.getModel());
        vehicle.setConsumptionRate(vehicleDto.getConsumptionRate());

        VehicleType vehicleType = vehicleTypeRepository.findById(vehicleDto.getVehicleTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("VehicleType","id",vehicleDto.getVehicleTypeId()));
        vehicle.setVehicleType(vehicleType);

        vehicle.setFuelId(vehicleDto.getFuelId());

        return vehicle;
    }

    // Convert DTO to Entity for Update
    private void mapToEntityUpdate(VehicleDto vehicleDto, Vehicle vehicle) {
        vehicle.setManufacturer(vehicleDto.getManufacturer());
        vehicle.setModel(vehicleDto.getModel());
        vehicle.setConsumptionRate(vehicleDto.getConsumptionRate());

        VehicleType vehicleType = vehicleTypeRepository.findById(vehicleDto.getVehicleTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("VehicleType","id",vehicleDto.getVehicleTypeId()));
        vehicle.setVehicleType(vehicleType);

        vehicle.setFuelId(vehicleDto.getFuelId());

    }
}
