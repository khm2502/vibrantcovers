package com.vibrantcovers.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${kinde.issuer-url}")
    private String issuerUrl;

    @Value("${kinde.client-secret}")
    private String clientSecret;

    // For Kinde tokens, we'll validate against their issuer
    // For now, we'll use a simple validation approach
    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("sub", String.class));
    }

    public String extractUserEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            // For Kinde tokens, validate against their public key
            // For now, we'll decode without verification (not recommended for production)
            // In production, fetch Kinde's public keys and verify properly
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            // If token is invalid, return empty claims (no verification)
            // JJWT 0.12+: Jwts.claims() returns a ClaimsBuilder, so call build()
            return Jwts.claims().build();
        }
    }

    public Boolean validateToken(String token, String userId) {
        if (token == null) return false;
        try {
            final String extractedUserId = extractUserId(token);
            return (extractedUserId != null && extractedUserId.equals(userId));
        } catch (Exception e) {
            return false;
        }
    }

    private SecretKey getSigningKey() {
        // For Kinde, we'd fetch their public key
        // For now, use a placeholder (not secure - for development only)
        byte[] keyBytes = (clientSecret != null ? clientSecret : "placeholder-secret-key").getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}



