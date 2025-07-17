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

// ...existing code...

    //Generate TOKEN with username and company
    public String generateToken(String username, String navBar) {
        return Jwts.builder()
                .setSubject(username)
                .claim("navBar", navBar)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    //Extract company from TOKEN
    public String extractNavBar(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("navBar", String.class);
    }


    //Extract username from TOKEN
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //Validate TOKEN
    public boolean validateToken(String token) {
        try {
            System.out.println("------[JWT UTIL] Validating token: " + token);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            System.out.println("-------[JWT UTIL] Token is valid!");
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    
}
