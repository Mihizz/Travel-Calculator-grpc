package com.fon.master.calculation_service.controller;

import com.fon.master.calculation_service.payload.TripDto;
import com.fon.master.calculation_service.service.impl.TripServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@WebMvcTest(TripController.class)
class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TripServiceImpl tripService;

    @Autowired
    private ObjectMapper objectMapper;

    private TripDto tripDto;

    @BeforeEach
    void setUp() {
        tripDto = new TripDto();
        tripDto.setCityName1("City1");
        tripDto.setCityName2("City2");
        tripDto.setConsumptionRate(8.0);
        tripDto.setFuelId(1L);
        tripDto.setTripDistance(150.0);
        tripDto.setTime("2 hours");
    }

    @Test
    void testCalculateTrip() throws Exception {
        Mockito.when(tripService.calculateTrip(Mockito.any(TripDto.class))).thenReturn(tripDto);

        mockMvc.perform(post("/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cityName1", is(tripDto.getCityName1())))
                .andExpect(jsonPath("$.cityName2", is(tripDto.getCityName2())))
                .andExpect(jsonPath("$.consumptionRate", is(tripDto.getConsumptionRate())))
                .andExpect(jsonPath("$.fuelId", is(tripDto.getFuelId().intValue())))
                .andExpect(jsonPath("$.tripDistance", is(tripDto.getTripDistance())))
                .andExpect(jsonPath("$.time", is(tripDto.getTime())));
    }

    @Test
    void testGetAllTrips() throws Exception {
        Mockito.when(tripService.getAllTrips()).thenReturn(Collections.singletonList(tripDto));

        mockMvc.perform(get("/trips")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].cityName1", is(tripDto.getCityName1())))
                .andExpect(jsonPath("$[0].cityName2", is(tripDto.getCityName2())))
                .andExpect(jsonPath("$[0].consumptionRate", is(tripDto.getConsumptionRate())))
                .andExpect(jsonPath("$[0].fuelId", is(tripDto.getFuelId().intValue())))
                .andExpect(jsonPath("$[0].tripDistance", is(tripDto.getTripDistance())))
                .andExpect(jsonPath("$[0].time", is(tripDto.getTime())));
    }
}

