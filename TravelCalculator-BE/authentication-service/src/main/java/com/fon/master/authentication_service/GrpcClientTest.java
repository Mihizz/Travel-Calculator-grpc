package com.fon.master.authentication_service;

import com.fon.master.proto.CountryServiceGrpc;
import com.fon.master.proto.ProtoCountry;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClientTest {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9000)
                .usePlaintext()
                .build();
        CountryServiceGrpc.CountryServiceBlockingStub stub = CountryServiceGrpc.newBlockingStub(channel);

        ProtoCountry request = ProtoCountry.newBuilder()
                                           .setCountryId(1)
                                           .build();

        ProtoCountry response = stub.getCountry(request);
        System.out.println("Response received: " + response);

        channel.shutdown();
    }
}