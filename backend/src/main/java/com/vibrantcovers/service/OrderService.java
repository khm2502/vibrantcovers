package com.vibrantcovers.service;

import com.vibrantcovers.entity.Order;
import com.vibrantcovers.entity.OrderStatus;
import com.vibrantcovers.entity.User;
import com.vibrantcovers.repository.OrderRepository;
import com.vibrantcovers.repository.OrderRepositoryImpl;
import com.vibrantcovers.repository.UserRepository;
import com.vibrantcovers.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderRepositoryImpl orderRepositoryImpl;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final ConfigurationService configurationService;
    
    public Order createOrGetOrder(String userId, String configurationId) {
        Optional<Order> existingOrder = orderRepository.findByUserIdAndConfigurationId(userId, configurationId);
        
        if (existingOrder.isPresent()) {
            return existingOrder.get();
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        var configuration = configurationService.getConfiguration(configurationId);
        double amount = paymentService.calculatePrice(configuration) / 100.0;
        
        Order order = new Order();
        order.setId(IdGenerator.generateId());
        order.setUser(user);
        order.setConfiguration(configuration);
        order.setAmount(amount);
        order.setIsPaid(false);
        order.setStatus(OrderStatus.AWAITING_SHIPMENT);
        
        return orderRepository.save(order);
    }
    
    public Order getOrder(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    public Order getOrderWithRelations(String orderId) {
        // Use EntityGraph or JOIN FETCH to load relations
        return orderRepository.findById(orderId)
                .map(order -> {
                    // Force loading of lazy relations
                    order.getConfiguration();
                    order.getUser();
                    order.getShippingAddress();
                    order.getBillingAddress();
                    return order;
                })
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    public Order getOrderByUserAndId(String userId, String orderId) {
        Order order = getOrderWithRelations(orderId);
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("Order does not belong to user");
        }
        return order;
    }
    
    public List<Order> getPaidOrdersSince(LocalDateTime since) {
        return orderRepositoryImpl.findPaidOrdersSinceNative(since);
    }
    
    public Double getTotalRevenueSince(LocalDateTime since) {
        return orderRepositoryImpl.sumPaidOrdersSinceNative(since);
    }
    
    @Transactional
    public Order updateOrderStatus(String orderId, OrderStatus status) {
        Order order = getOrder(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order markOrderAsPaid(String orderId) {
        Order order = getOrder(orderId);
        order.setIsPaid(true);
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }
}

