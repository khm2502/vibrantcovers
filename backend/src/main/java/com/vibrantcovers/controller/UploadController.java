package com.vibrantcovers.controller;

import com.vibrantcovers.entity.Configuration;
import com.vibrantcovers.service.ConfigurationService;
import com.vibrantcovers.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {
    
    private final UploadService uploadService;
    private final ConfigurationService configurationService;
    
    @PostMapping
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "configId", required = false) String configId) {
        try {
            UploadService.ImageUploadResult result = uploadService.uploadImage(file, configId);
            
            if (configId == null) {
                // Create new configuration
                Configuration config = configurationService.createConfiguration(
                        result.getUrl(),
                        result.getWidth(),
                        result.getHeight()
                );
                
                Map<String, String> response = new HashMap<>();
                response.put("configId", config.getId());
                return ResponseEntity.ok(response);
            } else {
                // Update existing configuration with cropped image
                configurationService.updateCroppedImage(configId, result.getUrl());
                
                Map<String, String> response = new HashMap<>();
                response.put("configId", configId);
                return ResponseEntity.ok(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}

