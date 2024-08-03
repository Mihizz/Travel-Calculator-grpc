package com.fon.master.authentication_service.controller;

import com.fon.master.authentication_service.payload.VehicleDto;
import com.fon.master.authentication_service.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("accounts/{accountId}/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<VehicleDto> createVehicle(@PathVariable long accountId, @RequestBody VehicleDto vehicleDto) {
        return new ResponseEntity<>(vehicleService.createVehicle(accountId, vehicleDto), HttpStatus.CREATED);
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleDto> getVehicleById(@PathVariable long accountId, @PathVariable long vehicleId) {
        return new ResponseEntity<>(vehicleService.getVehicleById(accountId,vehicleId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<VehicleDto>> getVehiclesByAccountId(@PathVariable long accountId) {
        List<VehicleDto> vehicles = vehicleService.getVehicleByAccountId(accountId);
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @PutMapping("/{vehicleId}")
    public ResponseEntity<VehicleDto> updateVehicle(@PathVariable long accountId, @PathVariable long vehicleId, @RequestBody VehicleDto vehicleDto) {
        VehicleDto updatedVehicle = vehicleService.updateVehicle(accountId, vehicleId, vehicleDto);
        return new ResponseEntity<>(updatedVehicle, HttpStatus.OK);
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<String> deleteVehicle(@PathVariable long accountId, @PathVariable long vehicleId) {
        vehicleService.deleteVehicle(accountId, vehicleId);
        return new ResponseEntity<>("Vehicle deleted successfully!", HttpStatus.OK);
    }
}
