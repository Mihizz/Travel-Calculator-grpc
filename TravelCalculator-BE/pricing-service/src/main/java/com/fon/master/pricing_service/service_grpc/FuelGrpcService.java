package com.fon.master.pricing_service.service_grpc;

import com.fon.master.pricing_service.model.Fuel;
import com.fon.master.pricing_service.repository.FuelRepository;
import com.fon.master.proto.FuelServiceGrpc;
import com.fon.master.proto.ProtoFuel;
import com.fon.master.proto.exception.ResourceNotFoundException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class FuelGrpcService extends FuelServiceGrpc.FuelServiceImplBase {

    private final FuelRepository fuelRepository;

    public FuelGrpcService(FuelRepository fuelRepository) {
        this.fuelRepository = fuelRepository;
    }

    @Override
    public void getFuel(ProtoFuel request, StreamObserver<ProtoFuel> responseObserver) {
        Fuel fuel = fuelRepository.findById(Long.valueOf(request.getFuelId()))
                .orElseThrow(() -> new ResourceNotFoundException("Fuel", "id", String.valueOf(request.getFuelId())));

        ProtoFuel response = ProtoFuel.newBuilder()
                .setFuelId(fuel.getId().intValue())
                .setCountryId(fuel.getCountry().getId().intValue())
                .setFuelTypeId(fuel.getFuelType().getId().intValue())
                .setPrice((float)fuel.getPrice())
                .setUpdatedAt(fuel.getUpdatedAt().toString())
                .setFuelTypeName(fuel.getFuelType().getFuelName())
                .setFuelUnit(fuel.getFuelType().getFuelUnit())
                .setFuelCountryName(fuel.getCountry().getCountryName())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
