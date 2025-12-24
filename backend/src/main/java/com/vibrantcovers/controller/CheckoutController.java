package com.vibrantcovers.controller;

import com.stripe.exception.StripeException;
import com.vibrantcovers.dto.CheckoutRequest;
import com.vibrantcovers.dto.CheckoutResponse;
import com.vibrantcovers.entity.Configuration;
import com.vibrantcovers.entity.Order;
import com.vibrantcovers.service.ConfigurationService;
import com.vibrantcovers.service.OrderService;
import com.vibrantcovers.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class  CheckoutController {
    
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final ConfigurationService configurationService;
    
    @PostMapping
    public ResponseEntity<?> createCheckoutSession(
            @RequestBody CheckoutRequest request,
            @RequestHeader("X-User-Id") String userId) {
        try {
            // Get configuration
            Configuration configuration = configurationService.getConfiguration(request.getConfigId());
            
            // Create or get existing order
            Order order = orderService.createOrGetOrder(userId, request.getConfigId());
            
            // Create Stripe checkout session
            String checkoutUrl = paymentService.createCheckoutSession(
                    configuration,
                    userId,
                    order.getId()
            );
            
            return ResponseEntity.ok(new CheckoutResponse(checkoutUrl));
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body("Error creating checkout session: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

