package com.fon.master.calculation_service.service.impl;

import com.fon.master.calculation_service.exception.ResourceNotFoundException;
import com.fon.master.calculation_service.model.City;
import com.fon.master.calculation_service.model.Trip;
import com.fon.master.calculation_service.payload.TripDto;
import com.fon.master.calculation_service.repository.CityRepository;
import com.fon.master.calculation_service.repository.TripRepository;
import com.fon.master.calculation_service.service.TripService;
import com.fon.master.calculation_service.valueObjects.*;
import com.fon.master.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;

@Service
public class TripServiceImpl implements TripService {
    @Value("${google.api.key}")
    private String apiKey;

    private boolean isGrpc = false;

    ManagedChannel pricingChannel = ManagedChannelBuilder.forAddress("localhost", 9091)
            .usePlaintext()
            .build();

    ManagedChannel authenticationChannel = ManagedChannelBuilder.forAddress("localhost", 9092)
            .usePlaintext()
            .build();

    CurrencyServiceGrpc.CurrencyServiceBlockingStub currencyClient = CurrencyServiceGrpc.newBlockingStub(pricingChannel);
    CountryServiceGrpc.CountryServiceBlockingStub countryClient = CountryServiceGrpc.newBlockingStub(pricingChannel);
    FuelServiceGrpc.FuelServiceBlockingStub fuelClient = FuelServiceGrpc.newBlockingStub(pricingChannel);

    AccountServiceGrpc.AccountServiceBlockingStub accountClient = AccountServiceGrpc.newBlockingStub(authenticationChannel);
    VehicleServiceGrpc.VehicleServiceBlockingStub vehicleClient = VehicleServiceGrpc.newBlockingStub(authenticationChannel);

    @Autowired
    @Qualifier("plainRestTemplate")
    private RestTemplate plainRestTemplate;

    @Autowired
    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate restTemplate;

    private TripRepository tripRepository;
    private CityRepository cityRepository;

    public TripServiceImpl(TripRepository tripRepository, CityRepository cityRepository) {
        this.tripRepository = tripRepository;
        this.cityRepository = cityRepository;
    }


    public TripDto calculateTrip(TripDto tripDto) {
        //starting record of the executionTime
        long startTime = System.currentTimeMillis();

        //uncomment this to use grpc
        //isGrpc = true;

        try {
            Trip trip = mapToEntity(tripDto);
            DecimalFormat df = new DecimalFormat("#.00");
            String url;
//
//            //getting distance and time based on the cities and pay-tool inputted
//            if(tripDto.getCityId1() == null){
//                String avoid = trip.getPaytool() == 0 ? "highways" : "";
//                url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/directions/json")
//                        .queryParam("origin", trip.getCityName1())
//                        .queryParam("destination", trip.getCityName2())
//                        .queryParam("key", apiKey)
//                        .queryParam("avoid", avoid)
//                        .toUriString();
//            }
//            else{
//                String avoid = trip.getPaytool() == 0 ? "highways" : "";
//                url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/directions/json")
//                        .queryParam("origin", trip.getCity1Code().length() < 10 ? trip.getCityName1() : trip.getCity1Code())
//                        .queryParam("destination", trip.getCity2Code().length() < 10 ? trip.getCityName2() : trip.getCity2Code())
//                        .queryParam("key", apiKey)
//                        .queryParam("avoid", avoid)
//                        .toUriString();
//            }
//
//
//
//            String jsonResponse = plainRestTemplate.getForObject(url, String.class);
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode rootNode = objectMapper.readTree(jsonResponse);
//            trip.setTripDistance(Double.parseDouble(rootNode.at("/routes/0/legs/0/distance/value").asText()) / 1000.00);
//            trip.setTime(rootNode.at("/routes/0/legs/0/duration/text").asText());

            trip.setTripDistance(150.00);
            trip.setTime("Testing");

            Fuel fuel = new Fuel();
            Currency currency = new Currency();

            if(isGrpc){
                if (tripDto.getAccountId() != null) {
                    handleAccountTripGrpc(trip, tripDto, df, fuel, currency);
                } else {
                    handleGuestTripGrpc(trip, tripDto, df, fuel, currency);
                }
            }
            else{
                if (tripDto.getAccountId() != null) {
                    handleAccountTrip(trip, tripDto, df, fuel, currency);
                } else {
                    handleGuestTrip(trip, tripDto, df, fuel, currency);
                }
            }

            trip.setExecutionTime(System.currentTimeMillis() - startTime);
            Trip savedTrip = tripRepository.save(trip);
            return mapToDTO(savedTrip, fuel, currency);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //if the user is logged in
    private void handleAccountTrip(Trip trip, TripDto tripDto, DecimalFormat df, Fuel fuel, Currency currency) {
        Account account = restTemplate.getForObject("http://authentication-service/accounts/" + tripDto.getAccountId(), Account.class);
        Currency currency1 = restTemplate.getForObject("http://pricing-service/currencies/" + account.getCurrencyId(), Currency.class);

        trip.setAccountId(account.getId());
        trip.setCountryId(account.getCountryId());
        trip.setCurrencyId(currency.getId());

        currency.setId(currency1.getId());
        currency.setCurrencyName(currency1.getCurrencyName());
        currency.setExchangeRate(currency1.getExchangeRate());

        //if account choose his vehicle
        if (tripDto.getVehicleId() != null) {
            Vehicle vehicle = restTemplate.getForObject("http://authentication-service/accounts/" + tripDto.getAccountId() + "/vehicles/" + tripDto.getVehicleId(), Vehicle.class);
            Fuel fuel1 = restTemplate.getForObject("http://pricing-service/fuels/" + vehicle.getFuelId(), Fuel.class);

            trip.setVehicleId(vehicle.getId());
            trip.setSeats(vehicle.getSeats());
            trip.setConsumptionRate(vehicle.getConsumptionRate());

            fuel.setId(fuel1.getId());
            fuel.setFuelName(fuel1.getFuelName());
            fuel.setFuelTypeId(fuel1.getFuelTypeId());
            fuel.setFuelUnit(fuel1.getFuelUnit());
            fuel.setPriceInDinars(fuel1.getPriceInDinars());

            trip.setFuelId(fuel.getId());

            calculatePrices(trip, df, fuel, currency);
        }
        // if account inputted manually
        else {
            Fuel fuel1 = restTemplate.getForObject("http://pricing-service/fuels/" + tripDto.getFuelId(), Fuel.class);

            trip.setFuelId(fuel.getId());
            trip.setConsumptionRate(tripDto.getConsumptionRate());

            fuel.setId(fuel1.getId());
            fuel.setFuelName(fuel1.getFuelName());
            fuel.setFuelTypeId(fuel1.getFuelTypeId());
            fuel.setFuelUnit(fuel1.getFuelUnit());
            fuel.setPriceInDinars(fuel1.getPriceInDinars());

            calculatePrices(trip, df, fuel, currency);
        }
    }

    private void handleGuestTrip(Trip trip, TripDto tripDto, DecimalFormat df, Fuel fuel, Currency currency) {
        Country country = restTemplate.getForObject("http://pricing-service/countries/" + tripDto.getCountryId(), Country.class);
        Currency currency1 = restTemplate.getForObject("http://pricing-service/currencies/" + country.getBaseCurrencyId(), Currency.class);
        Fuel fuel1 = restTemplate.getForObject("http://pricing-service/fuels/" + tripDto.getFuelId(), Fuel.class);

        trip.setCountryId(country.getId());
        trip.setFuelId(fuel.getId());
        trip.setCurrencyId(currency.getId());
        trip.setConsumptionRate(tripDto.getConsumptionRate());

        currency.setId(currency1.getId());
        currency.setCurrencyName(currency1.getCurrencyName());
        currency.setExchangeRate(currency1.getExchangeRate());

        fuel.setId(fuel1.getId());
        fuel.setFuelName(fuel1.getFuelName());
        fuel.setFuelTypeId(fuel1.getFuelTypeId());
        fuel.setFuelUnit(fuel1.getFuelUnit());
        fuel.setPriceInDinars(fuel1.getPriceInDinars());

        calculatePrices(trip, df, fuel, currency);
    }

    //calculations for the fullPrice
    private void calculatePrices(Trip trip, DecimalFormat df, Fuel fuel, Currency currency) {
        double fuelSpent = (trip.getTripDistance() / 100.00) * trip.getConsumptionRate();
        double price =   fuelSpent * fuel.getPriceInDinars();
        double shortPrice = Double.parseDouble(df.format(price));

        trip.setFuelSpent((int)fuelSpent);
        trip.setFullPrice(Double.parseDouble(df.format(shortPrice / currency.getExchangeRate())));
    }

    private void handleAccountTripGrpc(Trip trip, TripDto tripDto, DecimalFormat df, Fuel fuel, Currency currency) {
        ProtoAccount accountRequest = ProtoAccount.newBuilder().setAccountId(tripDto.getAccountId().intValue()).build();
        ProtoAccount accountResponse = accountClient.getAccount(accountRequest);

        ProtoCurrency currencyRequest = ProtoCurrency.newBuilder().setCurrencyId(accountResponse.getCurrencyId()).build();
        ProtoCurrency currencyResponse = currencyClient.getCurrency(currencyRequest);

        trip.setAccountId((long) accountResponse.getAccountId());
        trip.setCountryId((long) accountResponse.getCountryId());
        trip.setCurrencyId(currency.getId());

        currency.setId((long) currencyResponse.getCurrencyId());
        currency.setCurrencyName(currencyResponse.getCurrencyName());
        currency.setExchangeRate(currencyResponse.getExchangeRate());

        //if account choose his vehicle
        if (tripDto.getVehicleId() != null) {
            ProtoVehicle vehicleRequest = ProtoVehicle.newBuilder().setVehicleId(tripDto.getVehicleId().intValue()).setAccountId(trip.getAccountId().intValue()).build();
            ProtoVehicle vehicleResponse = vehicleClient.getVehicle(vehicleRequest);

            ProtoFuel fuelRequest = ProtoFuel.newBuilder().setFuelId(vehicleResponse.getFuelId()).build();
            ProtoFuel fuelResponse = fuelClient.getFuel(fuelRequest);

            trip.setVehicleId((long) vehicleResponse.getVehicleId());
            trip.setSeats(vehicleResponse.getSeats());
            trip.setConsumptionRate(vehicleResponse.getConsumptionRate());

            fuel.setId((long) fuelResponse.getFuelId());
            fuel.setFuelName(fuelResponse.getFuelTypeName());
            fuel.setFuelTypeId((long) fuelResponse.getFuelTypeId());
            fuel.setFuelUnit(fuelResponse.getFuelUnit());
            fuel.setPriceInDinars(fuelResponse.getPrice());

            trip.setFuelId(fuel.getId());

            calculatePrices(trip, df, fuel, currency);
        }
        // if account inputted manually
        else {
            ProtoFuel fuelRequest = ProtoFuel.newBuilder().setFuelId(tripDto.getFuelId().intValue()).build();
            ProtoFuel fuelResponse = fuelClient.getFuel(fuelRequest);

            trip.setFuelId(fuel.getId());
            trip.setConsumptionRate(tripDto.getConsumptionRate());

            fuel.setId((long) fuelResponse.getFuelId());
            fuel.setFuelName(fuelResponse.getFuelTypeName());
            fuel.setFuelTypeId((long) fuelResponse.getFuelTypeId());
            fuel.setFuelUnit(fuelResponse.getFuelUnit());
            fuel.setPriceInDinars(fuelResponse.getPrice());

            calculatePrices(trip, df, fuel, currency);
        }
    }

    private void handleGuestTripGrpc(Trip trip, TripDto tripDto, DecimalFormat df, Fuel fuel, Currency currency) {

        ProtoCountry countryRequest = ProtoCountry.newBuilder().setCountryId(tripDto.getCountryId().intValue()).build();
        ProtoCountry countryResponse = countryClient.getCountry(countryRequest);

        ProtoCurrency currencyRequest = ProtoCurrency.newBuilder().setCurrencyId(countryResponse.getBaseCurrencyId()).build();
        ProtoCurrency currencyResponse = currencyClient.getCurrency(currencyRequest);

        ProtoFuel fuelRequest = ProtoFuel.newBuilder().setFuelId(tripDto.getFuelId().intValue()).build();
        ProtoFuel fuelResponse = fuelClient.getFuel(fuelRequest);

        trip.setCountryId((long) countryResponse.getCountryId());
        trip.setFuelId(fuel.getId());
        trip.setCurrencyId(currency.getId());
        trip.setConsumptionRate(tripDto.getConsumptionRate());

        currency.setId((long) currencyResponse.getCurrencyId());
        currency.setCurrencyName(currencyResponse.getCurrencyName());
        currency.setExchangeRate(currencyResponse.getExchangeRate());

        fuel.setId((long) fuelResponse.getFuelId());
        fuel.setFuelName(fuelResponse.getFuelTypeName());
        fuel.setFuelTypeId((long) fuelResponse.getFuelTypeId());
        fuel.setFuelUnit(fuelResponse.getFuelUnit());
        fuel.setPriceInDinars(fuelResponse.getPrice());

        calculatePrices(trip, df, fuel, currency);
    }


    //from entity to DTO
    private TripDto mapToDTO(Trip trip, Fuel fuel, Currency currency) {
        TripDto tripDto = new TripDto();

        tripDto.setId(trip.getId());
        tripDto.setFuelId(trip.getFuelId());
        tripDto.setCountryId(trip.getCountryId());
        tripDto.setCurrencyId(trip.getCurrencyId());
        tripDto.setVehicleId(trip.getVehicleId());
        tripDto.setAccountId(trip.getAccountId());
        tripDto.setTripDistance(trip.getTripDistance());
        tripDto.setTime(trip.getTime());
        tripDto.setPaytool(trip.getPaytool());
        tripDto.setCityId1(trip.getCity1() != null ? trip.getCity1().getId() : null);
        tripDto.setCityId2(trip.getCity2() != null ? trip.getCity2().getId() : null);
        tripDto.setCityName1(trip.getCityName1().replace("_", " "));
        tripDto.setCityName2(trip.getCityName2().replace("_", " "));
        tripDto.setConsumptionRate(trip.getConsumptionRate());
        tripDto.setSeats(trip.getSeats());
        tripDto.setFuelSpent(trip.getFuelSpent());
        tripDto.setFullPrice(trip.getFullPrice());
        tripDto.setExecutionTime(trip.getExecutionTime());

        tripDto.setCurrencyId(currency.getId());
        tripDto.setExchangeRate(currency.getExchangeRate());
        tripDto.setCurrencySymbol(tripDto.getCurrencySymbol());

        tripDto.setFuelUnit(fuel.getFuelUnit());
        tripDto.setFuelPriceInDinars(fuel.getPriceInDinars());

        return tripDto;
    }

    //from DTO to entity
    private Trip mapToEntity(TripDto tripDto) {
        Trip trip = new Trip();

        trip.setPaytool(tripDto.getPaytool());

        if (tripDto.getCityId1() != null) {
            City city1 = cityRepository.findById(tripDto.getCityId1())
                    .orElseThrow(() -> new ResourceNotFoundException("City", "id", tripDto.getCityId1()));
            trip.setCity1(city1);
            trip.setCityName1(city1.getCityName());
            trip.setCity1Code("place_id:" + city1.getGoogleMapsCode());

        } else if (tripDto.getCityName1() != null) {
            trip.setCityName1(tripDto.getCityName1().replace(" ", "_"));

        } else {
            throw new IllegalArgumentException("Either cityId1 or cityName1 must be provided");
        }

        if (tripDto.getCityId2() != null) {
            City city2 = cityRepository.findById(tripDto.getCityId2())
                    .orElseThrow(() -> new ResourceNotFoundException("City", "id", tripDto.getCityId2()));
            trip.setCity2(city2);
            trip.setCityName2(city2.getCityName());
            trip.setCity2Code("place_id:" + city2.getGoogleMapsCode());

        } else if (tripDto.getCityName2() != null) {
            trip.setCityName2(tripDto.getCityName2().replace(" ", "_"));

        } else {
            throw new IllegalArgumentException("Either cityId2 or cityName2 must be provided");
        }

        return trip;
    }

}
