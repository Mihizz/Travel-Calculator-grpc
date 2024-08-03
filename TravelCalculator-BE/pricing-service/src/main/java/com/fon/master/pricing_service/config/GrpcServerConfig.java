package com.fon.master.pricing_service.config;

import com.fon.master.pricing_service.service_grpc.CountryGrpcService;
import com.fon.master.pricing_service.service_grpc.CurrencyGrpcService;
import com.fon.master.pricing_service.service_grpc.FuelGrpcService;
import com.fon.master.proto.FuelServiceGrpc;
import com.fon.master.proto.ProtoApplication;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GrpcServerConfig {

    @Value("${grpc.server.port:9091}")
    private int grpcPort;

    @Bean
    public Server grpcServer(CountryGrpcService countryGrpcService, CurrencyGrpcService currencyGrpcService, FuelGrpcService fuelGrpcService) {
        return ServerBuilder.forPort(grpcPort)
                .addService(countryGrpcService)
                .addService(currencyGrpcService)
                .addService(fuelGrpcService)
                .build();
    }
}
