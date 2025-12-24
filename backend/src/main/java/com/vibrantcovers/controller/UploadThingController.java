package com.vibrantcovers.controller;

import com.vibrantcovers.dto.UploadThingCallbackRequest;
import com.vibrantcovers.entity.Configuration;
import com.vibrantcovers.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/uploadthing")
public class UploadThingController {
    
    private final ConfigurationService configurationService;
    private final WebClient webClient;
    
    public UploadThingController(ConfigurationService configurationService, WebClient.Builder webClientBuilder) {
        this.configurationService = configurationService;
        this.webClient = webClientBuilder.build();
    }
    
    /**
     * Callback endpoint that UploadThing calls after file upload completes
     * This matches the Next.js onUploadComplete handler
     */
    @PostMapping("/callback")
    public ResponseEntity<Map<String, String>> handleUploadComplete(
            @RequestBody UploadThingCallbackRequest request) {
        try {
            String fileUrl = request.getFile().getUrl();
            String configId = request.getMetadata() != null && 
                             request.getMetadata().getInput() != null ?
                             request.getMetadata().getInput().getConfigId() : null;
            
            // Get image dimensions
            int width = 500;
            int height = 500;
            try {
                BufferedImage image = ImageIO.read(new URL(fileUrl));
                if (image != null) {
                    width = image.getWidth();
                    height = image.getHeight();
                }
            } catch (Exception e) {
                System.err.println("Failed to get image dimensions: " + e.getMessage());
            }
            
            if (configId == null || configId.isEmpty()) {
                // Create new configuration
                Configuration config = configurationService.createConfiguration(fileUrl, width, height);
                
                Map<String, String> response = new HashMap<>();
                response.put("configId", config.getId());
                return ResponseEntity.ok(response);
            } else {
                // Update existing configuration with cropped image
                configurationService.updateCroppedImage(configId, fileUrl);
                
                Map<String, String> response = new HashMap<>();
                response.put("configId", configId);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

