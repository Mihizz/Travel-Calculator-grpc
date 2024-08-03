package com.fon.master.calculation_service.service;

import com.fon.master.calculation_service.valueObjects.CityResponseTemplateVO;
import com.fon.master.calculation_service.payload.CityDto;

import java.util.List;

public interface CityService {
    // CREATE
    CityDto createCity(CityDto cityDto);

    // GET ALL
    List<CityDto> getAllCities();

    //GET BY COUNTRY

    // GET BY ID
    CityDto getCityById(long id);

    // UPDATE
    CityDto updateCity(long id, CityDto cityDto);

    // DELETE
    void deleteCity(long id);

    CityResponseTemplateVO getCityWithCountry(long cityId);
}
