package com.fon.master.authentication_service.service_grpc;

import com.fon.master.authentication_service.exception.ResourceNotFoundException;
import com.fon.master.authentication_service.model.Account;
import com.fon.master.authentication_service.repository.AccountRepository;
import com.fon.master.proto.AccountServiceGrpc;
import com.fon.master.proto.ProtoAccount;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AccountGrpcService extends AccountServiceGrpc.AccountServiceImplBase {

    private final AccountRepository accountRepository;

    public AccountGrpcService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void getAccount(ProtoAccount request, StreamObserver<ProtoAccount> responseObserver) {
        Account account = accountRepository.findById(Long.valueOf(request.getAccountId()))
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", String.valueOf(request.getAccountId())));

        ProtoAccount response = ProtoAccount.newBuilder()
                .setAccountId(account.getId().intValue())
                .setCurrencyId((int)account.getCurrencyId())
                .setCountryId((int)account.getCountryId())
                .setEmail(account.getEmail())
                .setFirstName(account.getFirstName())
                .setLastName(account.getLastName())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
