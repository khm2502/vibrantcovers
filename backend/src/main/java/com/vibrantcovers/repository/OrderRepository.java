package com.vibrantcovers.repository;

import com.vibrantcovers.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserId(String userId);
    
    Optional<Order> findByUserIdAndConfigurationId(String userId, String configurationId);
    
    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.user " +
           "LEFT JOIN FETCH o.shippingAddress " +
           "WHERE o.isPaid = true AND o.createdAt >= :since ORDER BY o.createdAt DESC")
    List<Order> findPaidOrdersSince(LocalDateTime since);
    
    @Query("SELECT SUM(o.amount) FROM Order o WHERE o.isPaid = true AND o.createdAt >= :since")
    Double sumPaidOrdersSince(LocalDateTime since);
}

