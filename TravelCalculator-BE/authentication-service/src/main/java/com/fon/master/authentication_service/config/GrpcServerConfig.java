package com.fon.master.authentication_service.config;

import com.fon.master.authentication_service.service_grpc.AccountGrpcService;
import com.fon.master.authentication_service.service_grpc.VehicleGrpcService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GrpcServerConfig {

    @Value("${grpc.server.port:9092}")
    private int grpcPort;

    @Bean
    public Server grpcServer(AccountGrpcService accountGrpcService, VehicleGrpcService vehicleGrpcService) {
        return ServerBuilder.forPort(grpcPort)
                .addService(accountGrpcService)
                .addService(vehicleGrpcService)
                .build();
    }
}
