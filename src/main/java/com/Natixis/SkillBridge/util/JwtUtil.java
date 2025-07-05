package com.Natixis.SkillBridge.util;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); 

    private final long expirationMs = 3600000; 

    //Gera o token que vai ser usado
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }


    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //Valida o token
    public boolean validateToken(String token) {
        try {
            System.out.println("------[JWT UTIL] Validando token: " + token);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            System.out.println("-------[JWT UTIL] Token válido!");
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    
}
