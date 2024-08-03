package com.fon.master.calculation_service.service;

import com.fon.master.calculation_service.payload.CityDto;
import com.fon.master.calculation_service.payload.TripDto;
import com.fon.master.calculation_service.valueObjects.TripResponseTemplateVO;

import java.util.List;

public interface TripService {
    TripDto calculateTrip(TripDto tripDto);

    List<TripDto> getAllTrips();
}
