package com.vibrantcovers.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    
    @Value("${resend.api-key}")
    private String resendApiKey;
    
    private final WebClient webClient;
    
    public EmailService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    
    public void sendOrderConfirmationEmail(String toEmail, String orderId, String orderDate, Map<String, String> shippingAddress) {
        // Resend API endpoint
        String url = "https://api.resend.com/emails";
        
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("from", "VibrantCovers <onboarding@resend.dev>");
        emailData.put("to", new String[]{toEmail});
        emailData.put("subject", "Thanks for your order!");
        
        // Build HTML email body
        String htmlBody = buildOrderConfirmationEmail(orderId, orderDate, shippingAddress);
        emailData.put("html", htmlBody);
        
        try {
            webClient.post()
                    .uri(url)
                    .header("Authorization", "Bearer " + resendApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(emailData)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            // Log error but don't fail the webhook
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
    
    private String buildOrderConfirmationEmail(String orderId, String orderDate, Map<String, String> shippingAddress) {
        return "<html><body>" +
                "<h1>Thank you for your order!</h1>" +
                "<p>Order Number: " + orderId + "</p>" +
                "<p>Order Date: " + orderDate + "</p>" +
                "<h2>Shipping Address:</h2>" +
                "<p>" + shippingAddress.get("name") + "<br>" +
                shippingAddress.get("street") + "<br>" +
                shippingAddress.get("city") + ", " + shippingAddress.get("postalCode") + "<br>" +
                shippingAddress.get("country") + "</p>" +
                "<p>Your custom phone case is being prepared and will ship soon!</p>" +
                "</body></html>";
    }
}

