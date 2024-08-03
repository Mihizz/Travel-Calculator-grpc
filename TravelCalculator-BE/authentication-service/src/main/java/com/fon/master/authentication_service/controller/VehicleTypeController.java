package com.fon.master.authentication_service.controller;

import com.fon.master.authentication_service.payload.VehicleTypeDto;
import com.fon.master.authentication_service.service.VehicleTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicle-types")
public class VehicleTypeController {

    private VehicleTypeService vehicleTypeService;


    public VehicleTypeController(VehicleTypeService vehicleTypeService) {
        this.vehicleTypeService = vehicleTypeService;
    }

    @PostMapping
    public ResponseEntity<VehicleTypeDto> createVehicleType(@RequestBody VehicleTypeDto vehicleTypeDto) {
        return new ResponseEntity<>(vehicleTypeService.createVehicleType(vehicleTypeDto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<VehicleTypeDto> getAllVehicleTypes() {
        return vehicleTypeService.getAllVehicleTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleTypeDto> getVehicleTypeById(@PathVariable(value = "id") long id) {
        VehicleTypeDto vehicleTypeDto = vehicleTypeService.getVehicleTypeById(id);
        return new ResponseEntity<>(vehicleTypeDto, HttpStatus.OK);
    }
}
