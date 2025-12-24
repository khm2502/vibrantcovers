package com.vibrantcovers.controller;

import com.vibrantcovers.dto.UpdateOrderStatusRequest;
import com.vibrantcovers.entity.Order;
import com.vibrantcovers.entity.OrderStatus;
import com.vibrantcovers.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    @Value("${admin.email}")
    private String adminEmail;
    
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(
            @PathVariable String id,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        try {
            Order order;
            if (userId != null) {
                // Get order with user validation (for thank-you page)
                order = orderService.getOrderByUserAndId(userId, id);
            } else {
                order = orderService.getOrderWithRelations(id);
            }
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}/status")
    public ResponseEntity<?> getPaymentStatus(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId) {
        try {
            Order order = orderService.getOrderByUserAndId(userId, id);
            if (order.getIsPaid()) {
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.ok(false);
            }
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData(
            @RequestHeader("X-User-Email") String userEmail) {
        
        // Check if user is admin
        if (!userEmail.equals(adminEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        LocalDateTime lastWeek = LocalDateTime.now().minusDays(7);
        LocalDateTime lastMonth = LocalDateTime.now().minusDays(30);
        
        System.out.println("Dashboard query - lastWeek: " + lastWeek);
        System.out.println("Dashboard query - lastMonth: " + lastMonth);
        
        List<Order> orders = orderService.getPaidOrdersSince(lastWeek);
        Double lastWeekSum = orderService.getTotalRevenueSince(lastWeek);
        Double lastMonthSum = orderService.getTotalRevenueSince(lastMonth);
        
        System.out.println("Dashboard results - orders count: " + orders.size());
        System.out.println("Dashboard results - lastWeekSum: " + lastWeekSum);
        System.out.println("Dashboard results - lastMonthSum: " + lastMonthSum);
        
        Map<String, Object> response = new HashMap<>();
        response.put("orders", orders); // Frontend expects single orders array
        response.put("lastWeekSum", lastWeekSum);
        response.put("lastMonthSum", lastMonthSum);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable String id,
            @RequestBody UpdateOrderStatusRequest request) {
        try {
            Order order = orderService.updateOrderStatus(id, request.getStatus());
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

