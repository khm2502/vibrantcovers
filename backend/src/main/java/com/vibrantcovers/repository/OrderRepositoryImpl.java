package com.vibrantcovers.repository;

import com.vibrantcovers.entity.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderRepositoryImpl {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<Order> findPaidOrdersSinceNative(LocalDateTime since) {
        // Use JPQL with explicit joins to ensure proper entity loading
        String jpql = "SELECT DISTINCT o FROM Order o " +
                     "LEFT JOIN FETCH o.user u " +
                     "LEFT JOIN FETCH o.shippingAddress sa " +
                     "WHERE o.isPaid = true AND o.createdAt >= :since ORDER BY o.createdAt DESC";
        
        TypedQuery<Order> query = entityManager.createQuery(jpql, Order.class);
        query.setParameter("since", since);
        return query.getResultList();
    }
    
    public Double sumPaidOrdersSinceNative(LocalDateTime since) {
        String jpql = "SELECT COALESCE(SUM(o.amount), 0.0) FROM Order o WHERE o.isPaid = true AND o.createdAt >= :since";
        TypedQuery<Double> query = entityManager.createQuery(jpql, Double.class);
        query.setParameter("since", since);
        Double result = query.getSingleResult();
        return result != null ? result : 0.0;
    }
}

