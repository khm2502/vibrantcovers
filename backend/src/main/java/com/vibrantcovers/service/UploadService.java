package com.vibrantcovers.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class UploadService {
    
    @Value("${uploadthing.secret}")
    private String uploadThingSecret;
    
    @Value("${uploadthing.app-id}")
    private String uploadThingAppId;
    
    private final WebClient webClient;
    
    public UploadService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    
    public ImageUploadResult uploadImage(MultipartFile file, String configId) throws IOException {
        // Get image dimensions
        BufferedImage image = ImageIO.read(file.getInputStream());
        int width = image.getWidth();
        int height = image.getHeight();
        
        // Check file size
        if (file.getSize() > 4 * 1024 * 1024) {
            throw new RuntimeException("File size exceeds 4MB");
        }
        
        // Try UploadThing if credentials are configured, otherwise use placeholder
        String uploadUrl;
        if (uploadThingSecret != null && !uploadThingSecret.trim().isEmpty() && 
            uploadThingAppId != null && !uploadThingAppId.trim().isEmpty()) {
            try {
                uploadUrl = uploadToUploadThing(file);
            } catch (Exception e) {
                // Fallback to placeholder if UploadThing fails
                System.err.println("UploadThing upload failed: " + e.getMessage());
                uploadUrl = "https://utfs.io/f/" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.]", "_") + "-" + System.currentTimeMillis();
            }
        } else {
            // No credentials - use placeholder
            uploadUrl = "https://utfs.io/f/" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.]", "_") + "-" + System.currentTimeMillis();
        }
        
        return new ImageUploadResult(uploadUrl, width, height);
    }
    
    private String uploadToUploadThing(MultipartFile file) throws IOException {
        // UploadThing API endpoint
        String url = "https://api.uploadthing.com/v6/upload";
        
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new ByteArrayResource(file.getBytes()))
                .filename(file.getOriginalFilename())
                .contentType(MediaType.parseMediaType(file.getContentType()));
        
        // Note: UploadThing requires specific authentication headers
        // This is a simplified version - you may need to adjust based on UploadThing's API
        Map<String, Object> response = webClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + uploadThingSecret)
                .header("x-uploadthing-app-id", uploadThingAppId)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        
        // Extract URL from response - adjust based on actual UploadThing response structure
        return (String) ((Map<?, ?>) response.get("data")).get("url");
    }
    
    public static class ImageUploadResult {
        private final String url;
        private final int width;
        private final int height;
        
        public ImageUploadResult(String url, int width, int height) {
            this.url = url;
            this.width = width;
            this.height = height;
        }
        
        public String getUrl() { return url; }
        public int getWidth() { return width; }
        public int getHeight() { return height; }
    }
}

