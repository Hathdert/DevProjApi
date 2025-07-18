package com.Natixis.SkillBridge.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Natixis.SkillBridge.util.JwtUtil;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //String path = request.getServletPath();
        //return path.startsWith("/auth/") || path.startsWith("/h2-console/");
        String path = request.getServletPath();
        return path.startsWith("/auth/") || path.startsWith("/h2-console/")
                || path.equals("/register");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        System.out.println("[JWT FILTER] Authorization header: " + authHeader);

        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            System.out.println("[JWT FILTER] Extracted token: " + jwt);
            
            if (jwtUtil.validateToken(jwt)) {
                username = jwtUtil.extractUsername(jwt);
                System.out.println("[JWT FILTER] Token valid. Username extracted: " + username);
            } else {
                logger.error("[JWT FILTER] Invalid JWT token.");
                System.out.println("[JWT FILTER] Token inválido!");
            }
        } else {
            logger.warn("[JWT FILTER] Authorization header is either missing or does not start with 'Bearer '.");
            System.out.println("[JWT FILTER] Authorization header is either missing or does not start with 'Bearer '.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("[JWT FILTER] Configurando autenticação para usuário: " + username);

            User userDetails = new User(username, "", List.of(new SimpleGrantedAuthority("ROLE_USER")));

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            logger.error("Authentication failed");
            System.out.println("Authentication failed");
        }

        filterChain.doFilter(request, response);
    }
}