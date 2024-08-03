package com.fon.master.authentication_service.service.impl;

import com.fon.master.authentication_service.exception.AuthServiceException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationInMilliseconds;

    public String generateToken(Authentication authentication) {
        return Jwts
                .builder()
                .subject(authentication.getName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMilliseconds))
                .signWith(getSignInKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parse(token);

            return true;
        } catch (SignatureException ex) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "Invalid JWT signature.");
        } catch (MalformedJwtException ex) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "Invalid JWT token.");
        } catch (ExpiredJwtException ex) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "Expired JWT token.");
        } catch (UnsupportedJwtException ex) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "Unsupported JWT token.");
        } catch (IllegalArgumentException ex) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public long getExpirationTime() {
        return jwtExpirationInMilliseconds;
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

