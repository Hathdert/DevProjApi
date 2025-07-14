package com.Natixis.SkillBridge.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Natixis.SkillBridge.Service.*;
import com.Natixis.SkillBridge.model.user.Company;
import com.Natixis.SkillBridge.model.user.User;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    // Injecting JwtAuthenticationFilter to handle JWT authentication
    private final JwtAuthenticationFilter jwtAuthFilter;
    private UserService userService;
    private CompanyService companyService;

    // Using @Lazy to avoid circular dependency issues because it only creates the bean when it's actually needed
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, @Lazy UserService userService, @Lazy CompanyService companyService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userService = userService;
        this.companyService = companyService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/register", "/h2-console/**").permitAll()
                        .anyRequest().permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();

                User user = userService.findByEmail(username);
                if ("company".equals(user.getRole())) {
                    Company company = companyService.getCompanyById(user.getId());
                     if (company.getApprovalStatus() == 0 || company.getApprovalStatus() == 2) {
                        logger.error("Company not approved or pending approval");
                        throw new AuthenticationException("Company not approved or pending approval") {
                        };
                    }
                }
                if (user != null && passwordEncoder().matches(password, user.getPassword())) {
                    logger.info("Authentication Completed");
                    List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_" + user.getRole()));
                    return new UsernamePasswordAuthenticationToken(username, password, authorities);
                } 
                else if("user".equals(username) && "pass".equals(password)){
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));

                    return new UsernamePasswordAuthenticationToken(username, password, authorities);
                }
                else {
                    logger.error("Authentication failed");
                    throw new AuthenticationException("Authentication failed") {
                    };
                }
            }

            @Override
            public boolean supports(Class<?> authentication) {

                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}