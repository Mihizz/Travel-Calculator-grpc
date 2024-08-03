package com.fon.master.authentication_service.service.impl;

import com.fon.master.authentication_service.exception.ResourceNotFoundException;
import com.fon.master.authentication_service.model.Account;
import com.fon.master.authentication_service.model.Role;
import com.fon.master.authentication_service.model.Vehicle;
import com.fon.master.authentication_service.payload.AccountDto;
import com.fon.master.authentication_service.payload.RegisterDto;
import com.fon.master.authentication_service.repository.RoleRepository;
import com.fon.master.authentication_service.repository.VehicleRepository;
import com.fon.master.authentication_service.service.AccountService;
import com.fon.master.authentication_service.exception.BadRequestException;
import com.fon.master.authentication_service.repository.AccountRepository;
import com.fon.master.authentication_service.service.VehicleService;
import com.fon.master.authentication_service.valueObjects.Country;
import com.fon.master.authentication_service.valueObjects.Currency;

import com.fon.master.proto.CountryServiceGrpc;
import com.fon.master.proto.CurrencyServiceGrpc;
import com.fon.master.proto.ProtoCountry;
import com.fon.master.proto.ProtoCurrency;
import org.springframework.security.core.Authentication;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9091)
            .usePlaintext()
            .build();

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    CurrencyServiceGrpc.CurrencyServiceBlockingStub currencyClient = CurrencyServiceGrpc.newBlockingStub(channel);
    CountryServiceGrpc.CountryServiceBlockingStub countryClient = CountryServiceGrpc.newBlockingStub(channel);

    private AccountRepository accountRepository;

    private VehicleRepository vehicleRepository;
    private VehicleService vehicleService;

    @Autowired
    private RestTemplate restTemplate;

    public AccountServiceImpl(RoleRepository roleRepository, PasswordEncoder passwordEncoder, AccountRepository accountRepository, VehicleRepository vehicleRepository, VehicleService vehicleService) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.vehicleRepository = vehicleRepository;
        this.vehicleService = vehicleService;
    }

    @Override
    public AccountDto createAccount(RegisterDto registerDto) {
        // check if email already exists
        if (accountRepository.existsByEmail(registerDto.getEmail())) {
            throw new BadRequestException("Email already exists.");
        }

        Account account =new Account();

        account.setEmail(registerDto.getEmail());
        account.setFirstName(registerDto.getFirstName());
        account.setLastName(registerDto.getLastName());
        account.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        account.setCountryId(registerDto.getCountryId());

        Country country = restTemplate.getForObject("http://pricing-service/countries/" + registerDto.getCountryId()
                ,Country.class);

        account.setCurrencyId(country.getBaseCurrencyId());

        Role userRole = roleRepository.findByName("user").orElseThrow(
                () -> new ResourceNotFoundException("Role", "name", "user")
        );

        Set<Role> roles = new HashSet<>();

        roles.add(userRole);

        account.setRoles(roles);

        //Save account
        Account account1 = accountRepository.save(account);

        return mapToDTO(account1);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();

        return accounts.stream()
                .map(account -> {
                    Country country = restTemplate.getForObject(
                            "http://pricing-service/countries/" + account.getCountryId(),
                            Country.class);

                    Currency currency = restTemplate.getForObject(
                            "http://pricing-service/currencies/" + account.getCurrencyId(),
                            Currency.class);

                    account.setCurrencyId(currency.getId());
                    account.setCountryId(country.getId());

                    account.setRoles(accountRepository.findRoleNamesByAccountId(account.getId()));

                    return mapToDTOWithValueObjects(account, country, currency);
                })
                .collect(Collectors.toList());

    }

    @Override
    public List<AccountDto> getAllAccountsGrpc() {
        List<Account> accounts = accountRepository.findAll();

        return accounts.stream()
                .map(account -> {
                    ProtoCurrency currencyRequest = ProtoCurrency.newBuilder().setCurrencyId((int)account.getCurrencyId()).build();
                    ProtoCurrency currencyResponse = currencyClient.getCurrency(currencyRequest);

                    ProtoCountry countryRequest = ProtoCountry.newBuilder().setCountryId((int)account.getCountryId()).build();
                    ProtoCountry countryResponse = countryClient.getCountry(countryRequest);

                    account.setCurrencyId(currencyResponse.getCurrencyId());
                    account.setCountryId(countryResponse.getCountryId());

                    account.setRoles(accountRepository.findRoleNamesByAccountId(account.getId()));

                    return mapToDTOWithValueObjects(account, countryResponse, currencyResponse);
                })
                .collect(Collectors.toList());

    }

    @Override
    public AccountDto getAccountById(long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException("Account","id",accountId));

        Country country = restTemplate.getForObject(
                "http://pricing-service/countries/" + account.getCountryId(),
                Country.class);

        Currency currency = restTemplate.getForObject(
                "http://pricing-service/currencies/" + account.getCurrencyId(),
                Currency.class);

        account.setCurrencyId(currency.getId());
        account.setCountryId(country.getId());

        account.setRoles(accountRepository.findRoleNamesByAccountId(accountId));

        return mapToDTOWithValueObjects(account, country, currency);
    }

    @Override
    public AccountDto getAccountByIdGrpc(long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException("Account","id",accountId));

        ProtoCurrency currencyRequest = ProtoCurrency.newBuilder().setCurrencyId((int)account.getCurrencyId()).build();
        ProtoCurrency currencyResponse = currencyClient.getCurrency(currencyRequest);

        ProtoCountry countryRequest = ProtoCountry.newBuilder().setCountryId((int)account.getCountryId()).build();
        ProtoCountry countryResponse = countryClient.getCountry(countryRequest);

        account.setCurrencyId(currencyResponse.getCurrencyId());
        account.setCountryId(countryResponse.getCountryId());

        account.setRoles(accountRepository.findRoleNamesByAccountId(accountId));

        return mapToDTOWithValueObjects(account, countryResponse, currencyResponse);
    }

    @Override
    public AccountDto getAccountByEmail(String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Account","email", 1L));

        return mapToDTO(account);
    }

    @Override
    public AccountDto updateAccount(long accountId, AccountDto accountDto) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException("Account","id",accountId));

        account.setEmail(accountDto.getEmail());
        account.setPassword(accountDto.getPassword());
        account.setFirstName(accountDto.getFirstName());
        account.setLastName(accountDto.getLastName());

        account.setCountryId(accountDto.getCountryId());
        account.setCurrencyId(accountDto.getCurrencyId());

        var accountVehicles = vehicleRepository.findByAccountId(accountId);

        if(!accountVehicles.isEmpty()){
            for (Vehicle vehicle: accountVehicles) {
                vehicleService.updateVehicleCountry(accountId, vehicle.getId(), account.getCountryId());
            }
        }

        Account account1 = accountRepository.save(account);

        return mapToDTO(account1);
    }

    @Override
    public void deleteAccount(long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException("Account","id",accountId));

        accountRepository.delete(account);
    }

    @Override
    public AccountDto getCurrent() {
        Account account = getCurrentAccount();

        Country country = restTemplate.getForObject(
                "http://pricing-service/countries/" + account.getCountryId(),
                Country.class);

        Currency currency = restTemplate.getForObject(
                "http://pricing-service/currencies/" + account.getCurrencyId(),
                Currency.class);

        account.setCurrencyId(currency.getId());
        account.setCountryId(country.getId());

        account.setRoles(accountRepository.findRoleNamesByAccountId(account.getId()));

        return mapToDTOWithValueObjects(account, country, currency);
    }

    private Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String currentEmail = authentication.getName();

        return accountRepository.findByEmail(currentEmail).orElseThrow(
                () -> new ResourceNotFoundException("Account", "email", currentEmail)
        );
    }

    //covert entity to DTO
    private AccountDto mapToDTO(Account account){
        AccountDto accountDto = new AccountDto();

        accountDto.setId(account.getId());
        accountDto.setEmail(account.getEmail());
        accountDto.setFirstName(account.getFirstName());
        accountDto.setLastName(account.getLastName());
        accountDto.setPassword(account.getPassword());

        accountDto.setCountryId(account.getCountryId());
        accountDto.setCurrencyId(account.getCurrencyId());

        return accountDto;
    }

    private AccountDto mapToDTOWithValueObjects(Account account, ProtoCountry country, ProtoCurrency currency){
        AccountDto accountDto = new AccountDto();

        accountDto.setId(account.getId());
        accountDto.setEmail(account.getEmail());
        accountDto.setFirstName(account.getFirstName());
        accountDto.setLastName(account.getLastName());
        accountDto.setPassword(account.getPassword());

        accountDto.setCountryId(account.getCountryId());
        accountDto.setCurrencyId(account.getCurrencyId());

        accountDto.setCountryName(country.getCountryName());
        accountDto.setCurrencyName(currency.getCurrencyName());

        accountDto.setRoles(account.getRoles());

        return accountDto;
    }

    private AccountDto mapToDTOWithValueObjects(Account account, Country country, Currency currency){
        AccountDto accountDto = new AccountDto();

        accountDto.setId(account.getId());
        accountDto.setEmail(account.getEmail());
        accountDto.setFirstName(account.getFirstName());
        accountDto.setLastName(account.getLastName());
        accountDto.setPassword(account.getPassword());

        accountDto.setCountryId(account.getCountryId());
        accountDto.setCurrencyId(account.getCurrencyId());

        accountDto.setCountryName(country.getCountryName());
        accountDto.setCurrencyName(currency.getCurrencyName());

        accountDto.setRoles(account.getRoles());

        return accountDto;
    }

}
