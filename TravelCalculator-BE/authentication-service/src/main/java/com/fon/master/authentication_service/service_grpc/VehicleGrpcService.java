package com.fon.master.authentication_service.service_grpc;

import com.fon.master.authentication_service.exception.BlogAPIException;
import com.fon.master.authentication_service.exception.ResourceNotFoundException;
import com.fon.master.authentication_service.model.Account;
import com.fon.master.authentication_service.model.Vehicle;
import com.fon.master.authentication_service.repository.AccountRepository;
import com.fon.master.authentication_service.repository.VehicleRepository;
import com.fon.master.authentication_service.valueObjects.Fuel;
import com.fon.master.proto.ProtoVehicle;
import com.fon.master.proto.VehicleServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.http.HttpStatus;

@GrpcService
public class VehicleGrpcService extends VehicleServiceGrpc.VehicleServiceImplBase {

    private final VehicleRepository vehicleRepository;

    private final AccountRepository accountRepository;

    public VehicleGrpcService(VehicleRepository vehicleRepository, AccountRepository accountRepository) {
        this.vehicleRepository = vehicleRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void getVehicle(ProtoVehicle request, StreamObserver<ProtoVehicle> responseObserver) {
        Account account = accountRepository.findById((long) request.getAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("Account","id", (long) request.getAccountId()));

        Vehicle vehicle = vehicleRepository.findById(Long.valueOf(request.getVehicleId()))
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", String.valueOf(request.getVehicleId())));

        if(!(vehicle.getAccount().getId() == (account.getId()))){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Car does not belong to this account");
        }

        ProtoVehicle response = ProtoVehicle.newBuilder()
                .setVehicleId(vehicle.getId().intValue())
                .setAccountId(vehicle.getAccount().getId().intValue())
                .setFuelId(vehicle.getFuelId().intValue())
                .setVehicleTypeId(vehicle.getVehicleType().getId().intValue())
                .setManufacturer(vehicle.getManufacturer())
                .setModel(vehicle.getModel())
                .setConsumptionRate((float)vehicle.getConsumptionRate())
                .setSeats(vehicle.getVehicleType().getSeats())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
