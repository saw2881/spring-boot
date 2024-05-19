package com.subsaw.ilearn.security.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

@Service
public class JwtUtil {

    @Value("${jwt.issuer}")
    private static String issuer;

    @Value("${jwt.key}")
    private String key;

    private final static String ROLE_STRING = "roles";
    
    public String getToken(Authentication auth) {
        Assert.notNull(auth, "Authentication object cannot be null.");

        SecretKey signingKey = Keys.hmacShaKeyFor(key.getBytes());
       
        var issuedAt = Instant.now();
        var expiration = issuedAt.plus(24, ChronoUnit.HOURS);

        String token = Jwts.builder()
                    .subject(auth.getPrincipal().toString())
                    .issuer(issuer)
                    .issuedAt(Date.from(issuedAt))
                    .expiration(Date.from(expiration))
                    .claims(getClaims(auth))
                    .signWith(signingKey, Jwts.SIG.HS512)
                    .compact();
        
        return token;
    }

    public boolean validateToken(String token) {
        Assert.notNull(token, "Jwt token cannot be null");

        SecretKey signingKey = Keys.hmacShaKeyFor(key.getBytes());

        try {
            
            Jwts.parser().verifyWith(signingKey).build().parse(token);
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | SecurityException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    private Map<String, ?> getClaims(Authentication auth) {
        var rolesCollector = Collector.of(
            HashMap<String, String>::new, 
            (HashMap<String, String> map, String authority) -> {
                map.put("Claims", authority.toString());
            },
            (first, second) -> {
                String existingClaims = first.get(ROLE_STRING);
                String newClaim = second.get(ROLE_STRING);
                return (HashMap<String, String>)Map.of(ROLE_STRING, String.join(",", existingClaims, newClaim));
            }
        );

       return auth.getAuthorities().stream()
       .map(authority -> authority.getAuthority())
       .collect(rolesCollector);
    }
}
