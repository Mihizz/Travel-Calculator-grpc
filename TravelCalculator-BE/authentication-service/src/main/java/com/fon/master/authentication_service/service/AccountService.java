package com.fon.master.authentication_service.service;

import com.fon.master.authentication_service.payload.AccountDto;
import com.fon.master.authentication_service.payload.RegisterDto;

import java.util.List;

public interface AccountService {

    //CREATE
    AccountDto createAccount(RegisterDto registerDto);

    List<AccountDto> getAllAccounts();

    //GET ALL USERS (GRPC)
    List<AccountDto> getAllAccountsGrpc();

    // GET BY ID
    AccountDto getAccountById(long accountId);

    // GET BY ID (GRPC)
    AccountDto getAccountByIdGrpc(long accountId);

    // GET BY EMAIL
    AccountDto getAccountByEmail(String email);

    //UPDATE
    AccountDto updateAccount(long accountId, AccountDto accountDto);

    //DELETE
    void deleteAccount(long accountId);
}
