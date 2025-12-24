package com.vibrantcovers.repository;

import com.vibrantcovers.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserId(String userId);
    
    Optional<Order> findByUserIdAndConfigurationId(String userId, String configurationId);
}

