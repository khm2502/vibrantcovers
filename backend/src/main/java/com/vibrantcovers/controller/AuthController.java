package com.vibrantcovers.controller;

import com.vibrantcovers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    @PostMapping("/callback")
    public ResponseEntity<Map<String, Boolean>> handleAuthCallback(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Email") String email) {
        try {
            userService.createOrGetUser(userId, email);
            Map<String, Boolean> response = new HashMap<>();
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

