package com.fon.master.authentication_service.controller;

import com.fon.master.authentication_service.payload.AccountDto;
import com.fon.master.authentication_service.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) { this.accountService = accountService;}

    @GetMapping
    public List<AccountDto> getAllAccounts(){
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable(value = "id") long id) {
        return new ResponseEntity<>(accountService.getAccountById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable(value = "id") long id, @RequestBody AccountDto accountDto) {
        AccountDto updateAccount = accountService.updateAccount(id, accountDto);
        return new ResponseEntity<>(updateAccount, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<AccountDto> getCurrentAccount() {
        return new ResponseEntity<>(accountService.getCurrent(), HttpStatus.OK);
    }

}
