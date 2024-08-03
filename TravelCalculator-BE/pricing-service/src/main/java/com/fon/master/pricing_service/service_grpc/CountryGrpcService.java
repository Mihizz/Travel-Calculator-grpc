package com.fon.master.pricing_service.service_grpc;

import com.fon.master.pricing_service.model.Country;
import com.fon.master.pricing_service.repository.CountryRepository;
import com.fon.master.proto.CountryServiceGrpc;
import com.fon.master.proto.ProtoCountry;
import com.fon.master.proto.exception.ResourceNotFoundException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class CountryGrpcService extends CountryServiceGrpc.CountryServiceImplBase {

    private final CountryRepository countryRepository;

    public CountryGrpcService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void getCountry(ProtoCountry request, StreamObserver<ProtoCountry> responseObserver) {
        Country country = countryRepository.findById(Long.valueOf(request.getCountryId())).orElseThrow(() -> new ResourceNotFoundException("Country", "id", String.valueOf(request.getCountryId())));

        ProtoCountry response = ProtoCountry.newBuilder()
                .setCountryId(country.getId().intValue())
                .setCountryName(country.getCountryName())
                .setCountryNameEng(country.getCountryNameEng())
                .setBaseCurrencyId(country.getBaseCurrency().getId().intValue())
                .build();
        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }
}
