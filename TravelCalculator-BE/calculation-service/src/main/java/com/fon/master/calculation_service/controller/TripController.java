package com.fon.master.calculation_service.controller;

import com.fon.master.calculation_service.payload.TripDto;
import com.fon.master.calculation_service.service.impl.TripServiceImpl;
import com.fon.master.calculation_service.valueObjects.TripResponseTemplateVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trips")
public class TripController {

    private final TripServiceImpl tripServiceImpl;

    public TripController(TripServiceImpl tripServiceImpl) {
        this.tripServiceImpl = tripServiceImpl;
    }

    @PostMapping
    public ResponseEntity<TripDto> calculateTrip(@RequestBody TripDto tripDto) {
        return new ResponseEntity<>(tripServiceImpl.calculateTrip(tripDto), HttpStatus.CREATED);
    }
}
