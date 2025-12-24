package com.vibrantcovers.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Skip authentication for public endpoints
        String path = request.getRequestURI();
        if (path.startsWith("/api/webhooks") || 
            path.startsWith("/api/upload") ||
            path.startsWith("/api/uploadthing") ||
            path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Try to get token from Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userId = null;
        String userEmail = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                userId = jwtService.extractUserId(token);
                userEmail = jwtService.extractUserEmail(token);
            } catch (Exception e) {
                // Token invalid, continue without authentication
            }
        }

        // Fallback: Check custom headers (for testing/development)
        if (userId == null) {
            userId = request.getHeader("X-User-Id");
            userEmail = request.getHeader("X-User-Email");
        }

        // If we have user info, set authentication
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtService.validateToken(token, userId) || userId != null) {
                // Create authentication token
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
                // Store user info in request attributes for controllers
                request.setAttribute("userId", userId);
                request.setAttribute("userEmail", userEmail);
            }
        }

        filterChain.doFilter(request, response);
    }
}





