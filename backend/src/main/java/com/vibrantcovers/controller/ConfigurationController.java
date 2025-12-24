package com.vibrantcovers.controller;

import com.vibrantcovers.dto.SaveConfigurationRequest;
import com.vibrantcovers.entity.Configuration;
import com.vibrantcovers.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configurations")
@RequiredArgsConstructor
public class ConfigurationController {
    
    private final ConfigurationService configurationService;
    
    @GetMapping("/{id}")
    public ResponseEntity<Configuration> getConfiguration(@PathVariable String id) {
        try {
            Configuration configuration = configurationService.getConfiguration(id);
            return ResponseEntity.ok(configuration);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Configuration> saveConfiguration(
            @PathVariable String id,
            @RequestBody SaveConfigurationRequest request) {
        try {
            Configuration configuration = configurationService.updateConfiguration(id, request);
            return ResponseEntity.ok(configuration);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

