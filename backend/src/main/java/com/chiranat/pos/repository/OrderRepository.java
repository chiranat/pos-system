package com.chiranat.pos.repository;

import com.chiranat.pos.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findBySessionId(UUID sessionId);
    List<Order> findBySessionIn(java.util.Collection<com.chiranat.pos.model.DiningSession> sessions);
    List<Order> findByStatus(Order.OrderStatus status);
    List<Order> findBySessionIdAndStatusNot(UUID sessionId, Order.OrderStatus status);
}
