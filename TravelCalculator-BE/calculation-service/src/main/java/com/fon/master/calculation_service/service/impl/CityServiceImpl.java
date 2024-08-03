package com.fon.master.calculation_service.service.impl;

import com.fon.master.calculation_service.valueObjects.Country;
import com.fon.master.calculation_service.valueObjects.CityResponseTemplateVO;
import com.fon.master.calculation_service.exception.ResourceNotFoundException;
import com.fon.master.calculation_service.model.City;
import com.fon.master.calculation_service.payload.CityDto;
import com.fon.master.calculation_service.repository.CityRepository;
import com.fon.master.calculation_service.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Autowired
    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }


    @Override
    public CityDto createCity(CityDto cityDto) {
        City city = mapToEntity(cityDto);
        City savedCity = cityRepository.save(city);
        return mapToDTO(savedCity);
    }

    @Override
    public List<CityDto> getAllCities() {
        List<City> cities = cityRepository.findAll();
        return cities.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CityDto getCityById(long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City", "id", id));
        return mapToDTO(city);
    }

    @Override
    public CityDto updateCity(long id, CityDto cityDto) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City", "id", id));

        mapToEntityUpdate(cityDto, city);

        City updatedCity = cityRepository.save(city);
        return mapToDTO(updatedCity);
    }

    @Override
    public void deleteCity(long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City", "id", id));
        cityRepository.delete(city);
    }

    @Override
    public CityResponseTemplateVO getCityWithCountry(long cityId) {
        CityResponseTemplateVO vo = new CityResponseTemplateVO();
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City", "id", cityId));

        Country country = restTemplate.getForObject("http://pricing-service/countries/" + city.getCountryId()
                ,Country.class);

        vo.setCity(city);
        vo.setCountry(country);

        return vo;
    }

    // Convert Entity to DTO
    private CityDto mapToDTO(City city) {
        CityDto cityDto = new CityDto();

        cityDto.setId(city.getId());
        cityDto.setCityName(city.getCityName());

        cityDto.setCountryId(city.getCountryId());

        return cityDto;
    }

    // Convert DTO to Entity
    private City mapToEntity(CityDto cityDto) {
        City city = new City();

        city.setId(cityDto.getId());
        city.setCityName(cityDto.getCityName());
        city.setGoogleMapsCode(cityDto.getGoogleMapsCode());

        city.setCountryId(city.getCountryId());

        return city;
    }

    // Convert DTO to Entity for Update
    private void mapToEntityUpdate(CityDto cityDto, City city) {
        city.setCityName(cityDto.getCityName());
        city.setGoogleMapsCode(cityDto.getGoogleMapsCode());

        city.setCountryId(city.getCountryId());
    }
}
