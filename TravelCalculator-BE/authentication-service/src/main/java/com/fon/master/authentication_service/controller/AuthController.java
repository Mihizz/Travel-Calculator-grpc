package com.fon.master.authentication_service.controller;

import com.fon.master.authentication_service.payload.AccountDto;
import com.fon.master.authentication_service.payload.LoginDto;
import com.fon.master.authentication_service.payload.RegisterDto;
import com.fon.master.authentication_service.payload.TokenDto;
import com.fon.master.authentication_service.service.AccountService;
import com.fon.master.authentication_service.service.impl.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;


@Tag(name = "Authentication")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          AccountService accountService,
                          JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AccountDto> register(@RequestBody RegisterDto registerDto) {
        return new ResponseEntity<>(accountService.createAccount(registerDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()
                ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtService.generateToken(authentication);

        return new ResponseEntity<>(new TokenDto(token, jwtService.getExpirationTime()), HttpStatus.OK);
    }
}

