package com.fon.master.calculation_service.controller;

import com.fon.master.calculation_service.payload.CityDto;
import com.fon.master.calculation_service.service.CityService;
import com.fon.master.calculation_service.valueObjects.CityResponseTemplateVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CityControllerTest {

    @Mock
    private CityService cityService;

    @InjectMocks
    private CityController cityController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cityController).build();
    }

    @Test
    public void testCreateCity() throws Exception {
        CityDto cityDto = new CityDto();
        cityDto.setId(1L);
        cityDto.setCityName("Test City");
        when(cityService.createCity(any(CityDto.class))).thenReturn(cityDto);

        mockMvc.perform(post("/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cityName\": \"Test City\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cityName").value("Test City"));

        verify(cityService, times(1)).createCity(any(CityDto.class));
    }

    @Test
    public void testGetAllCities() throws Exception {
        CityDto cityDto1 = new CityDto();
        cityDto1.setId(1L);
        cityDto1.setCityName("City 1");
        CityDto cityDto2 = new CityDto();
        cityDto2.setId(2L);
        cityDto2.setCityName("City 2");
        List<CityDto> cities = Arrays.asList(cityDto1, cityDto2);

        when(cityService.getAllCities()).thenReturn(cities);

        mockMvc.perform(get("/cities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].cityName").value("City 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].cityName").value("City 2"));

        verify(cityService, times(1)).getAllCities();
    }

    @Test
    public void testGetCityById() throws Exception {
        CityDto cityDto = new CityDto();
        cityDto.setId(1L);
        cityDto.setCityName("Test City");

        when(cityService.getCityById(1L)).thenReturn(cityDto);

        mockMvc.perform(get("/cities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cityName").value("Test City"));

        verify(cityService, times(1)).getCityById(1L);
    }

    @Test
    public void testUpdateCity() throws Exception {
        CityDto cityDto = new CityDto();
        cityDto.setId(1L);
        cityDto.setCityName("Updated City");

        when(cityService.updateCity(eq(1L), any(CityDto.class))).thenReturn(cityDto);

        mockMvc.perform(put("/cities/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cityName\": \"Updated City\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cityName").value("Updated City"));

        verify(cityService, times(1)).updateCity(eq(1L), any(CityDto.class));
    }

    @Test
    public void testDeleteCity() throws Exception {
        doNothing().when(cityService).deleteCity(1L);

        mockMvc.perform(delete("/cities/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("City deleted successfully!"));

        verify(cityService, times(1)).deleteCity(1L);
    }

    @Test
    public void testGetCityWithCountry() throws Exception {
        CityResponseTemplateVO response = new CityResponseTemplateVO();
        when(cityService.getCityWithCountry(1L)).thenReturn(response);

        mockMvc.perform(get("/cities/all/1"))
                .andExpect(status().isOk());

        verify(cityService, times(1)).getCityWithCountry(1L);
    }
}

