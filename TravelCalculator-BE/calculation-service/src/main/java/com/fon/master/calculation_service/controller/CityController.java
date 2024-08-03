package com.fon.master.calculation_service.controller;

import com.fon.master.calculation_service.valueObjects.CityResponseTemplateVO;
import com.fon.master.calculation_service.payload.CityDto;
import com.fon.master.calculation_service.service.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    public ResponseEntity<CityDto> createCity(@RequestBody CityDto cityDto) {
        return new ResponseEntity<>(cityService.createCity(cityDto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<CityDto> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDto> getCityById(@PathVariable(value = "id") long id) {
        CityDto cityDto = cityService.getCityById(id);
        return new ResponseEntity<>(cityDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityDto> updateCity(@PathVariable(value = "id") long id,
                                              @RequestBody CityDto cityDto) {
        CityDto updatedCity = cityService.updateCity(id, cityDto);
        return new ResponseEntity<>(updatedCity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCity(@PathVariable(value = "id") long id) {
        cityService.deleteCity(id);
        return new ResponseEntity<>("City deleted successfully!", HttpStatus.OK);
    }

    @GetMapping("/all/{id}")
    public CityResponseTemplateVO getCityWithCountry(@PathVariable(value = "id")long cityId){
        return cityService.getCityWithCountry(cityId);
    }
}
