package com.vibrantcovers.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.vibrantcovers.entity.Order;
import com.vibrantcovers.entity.ShippingAddress;
import com.vibrantcovers.entity.BillingAddress;
import com.vibrantcovers.repository.ShippingAddressRepository;
import com.vibrantcovers.repository.BillingAddressRepository;
import com.vibrantcovers.service.OrderService;
import com.vibrantcovers.service.EmailService;
import com.vibrantcovers.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks/stripe")
@RequiredArgsConstructor
public class WebhookController {
    
    private final OrderService orderService;
    private final ShippingAddressRepository shippingAddressRepository;
    private final BillingAddressRepository billingAddressRepository;
    private final EmailService emailService;
    
    @Value("${stripe.webhook-secret}")
    private String webhookSecret;
    
    @PostMapping
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            
            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getDataObjectDeserializer()
                        .getObject()
                        .orElseThrow(() -> new RuntimeException("Unable to deserialize session"));
                
                String orderId = session.getMetadata().get("orderId");
                String userId = session.getMetadata().get("userId");
                
                if (orderId == null || userId == null) {
                    return ResponseEntity.badRequest().body("Missing metadata");
                }
                
                Order order = orderService.getOrder(orderId);
                
                // Create shipping address
                ShippingAddress shippingAddress = null;
                if (session.getShippingDetails() != null && session.getShippingDetails().getAddress() != null) {
                    shippingAddress = createShippingAddress(session);
                    shippingAddress = shippingAddressRepository.save(shippingAddress);
                }
                
                // Create billing address
                BillingAddress billingAddress = null;
                if (session.getCustomerDetails() != null && session.getCustomerDetails().getAddress() != null) {
                    billingAddress = createBillingAddress(session);
                    billingAddress = billingAddressRepository.save(billingAddress);
                }
                
                // Update order with addresses and mark as paid
                order.setShippingAddress(shippingAddress);
                order.setBillingAddress(billingAddress);
                order.setIsPaid(true);
                order = orderService.updateOrder(order); // Save order with addresses
                
                // Send email notification
                if (session.getCustomerDetails() != null && session.getCustomerDetails().getEmail() != null) {
                    Map<String, String> shippingAddressMap = new HashMap<>();
                    if (shippingAddress != null) {
                        shippingAddressMap.put("name", shippingAddress.getName());
                        shippingAddressMap.put("street", shippingAddress.getStreet());
                        shippingAddressMap.put("city", shippingAddress.getCity());
                        shippingAddressMap.put("postalCode", shippingAddress.getPostalCode());
                        shippingAddressMap.put("country", shippingAddress.getCountry());
                    }
                    
                    emailService.sendOrderConfirmationEmail(
                            session.getCustomerDetails().getEmail(),
                            orderId,
                            order.getCreatedAt().toString(),
                            shippingAddressMap
                    );
                }
                
                return ResponseEntity.ok("Webhook processed successfully");
            }
            
            return ResponseEntity.ok("Event type not handled");
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing webhook: " + e.getMessage());
        }
    }
    
    private ShippingAddress createShippingAddress(Session session) {
        ShippingAddress address = new ShippingAddress();
        address.setId(IdGenerator.generateId());
        address.setName(session.getCustomerDetails().getName());
        
        var shippingAddress = session.getShippingDetails().getAddress();
        address.setStreet(shippingAddress.getLine1());
        address.setCity(shippingAddress.getCity());
        address.setPostalCode(shippingAddress.getPostalCode());
        address.setCountry(shippingAddress.getCountry());
        address.setState(shippingAddress.getState());
        
        return address;
    }
    
    private BillingAddress createBillingAddress(Session session) {
        BillingAddress address = new BillingAddress();
        address.setId(IdGenerator.generateId());
        address.setName(session.getCustomerDetails().getName());
        
        var billingAddress = session.getCustomerDetails().getAddress();
        address.setStreet(billingAddress.getLine1());
        address.setCity(billingAddress.getCity());
        address.setPostalCode(billingAddress.getPostalCode());
        address.setCountry(billingAddress.getCountry());
        address.setState(billingAddress.getState());
        
        return address;
    }
}

