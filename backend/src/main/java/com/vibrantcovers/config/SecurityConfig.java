package com.vibrantcovers.config;

import com.vibrantcovers.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/webhooks/**").permitAll()
                .requestMatchers("/api/upload").permitAll()
                .requestMatchers("/api/uploadthing/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                // For now, allow these for easier frontend testing.
                // You can tighten this later when JWT integration is fully wired on the frontend.
                .requestMatchers("/api/configurations/**").permitAll()
                .requestMatchers("/api/checkout").permitAll()
                .requestMatchers("/api/orders/**").permitAll()
                .anyRequest().permitAll() // Allow other endpoints for now
            );
        
        return http.build();
    }
}

