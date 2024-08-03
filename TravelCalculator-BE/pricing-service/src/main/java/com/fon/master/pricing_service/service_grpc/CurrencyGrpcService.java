package com.fon.master.pricing_service.service_grpc;


import com.fon.master.pricing_service.model.Currency;
import com.fon.master.pricing_service.repository.CurrencyRepository;
import com.fon.master.proto.CurrencyServiceGrpc;
import com.fon.master.proto.ProtoCurrency;
import com.fon.master.proto.exception.ResourceNotFoundException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.format.DateTimeFormatter;

@GrpcService
public class CurrencyGrpcService extends CurrencyServiceGrpc.CurrencyServiceImplBase {

    private final CurrencyRepository currencyRepository;

    public CurrencyGrpcService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void getCurrency(ProtoCurrency request, StreamObserver<ProtoCurrency> responseObserver) {
        Currency currency = currencyRepository.findById(Long.valueOf(request.getCurrencyId())).orElseThrow(() -> new ResourceNotFoundException("Currency", "id", String.valueOf(request.getCurrencyId())));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        ProtoCurrency response = ProtoCurrency.newBuilder()
                .setCurrencyId(currency.getId().intValue())
                .setCurrencyName(currency.getCurrencyName())
                .setExchangeRate((float)currency.getExchangeRate())
                .setSymbol(currency.getSymbol())
                .setUpdatedAt(currency.getUpdatedAt().format(dateTimeFormatter))
                .build();
        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }
}
