package com.fon.master.api_gateway.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    public boolean validateToken(String token) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new RuntimeException("Invalid JWT signature.");
        } catch (MalformedJwtException ex) {
            throw new RuntimeException("Invalid JWT token.");
        } catch (ExpiredJwtException ex) {
            throw new RuntimeException("Expired JWT token.");
        } catch (UnsupportedJwtException ex) {
            throw new RuntimeException("Unsupported JWT token.");
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("JWT claims string is empty.");
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

