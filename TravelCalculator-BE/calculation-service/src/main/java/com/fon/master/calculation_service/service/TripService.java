package com.fon.master.calculation_service.service;

import com.fon.master.calculation_service.payload.TripDto;
import com.fon.master.calculation_service.valueObjects.TripResponseTemplateVO;

public interface TripService {
    TripDto calculateTrip(TripDto tripDto);

    //TripDto getAllTrips();
}
