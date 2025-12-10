package com.chiranat.pos.service;

import com.chiranat.pos.dto.OrderRequest;
import com.chiranat.pos.exception.ResourceNotFoundException;
import com.chiranat.pos.model.*;
import com.chiranat.pos.repository.DiningSessionRepository;
import com.chiranat.pos.repository.OrderRepository;
import com.chiranat.pos.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DiningSessionRepository diningSessionRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(OrderRequest request) {
        // 1. Validate Session
        DiningSession session = diningSessionRepository.findByAccessToken(request.getToken())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid session token"));

        if (session.getStatus() != DiningSession.SessionStatus.ACTIVE) {
            throw new IllegalStateException("Session is not active");
        }

        // 2. Create Order
        Order order = Order.builder()
                .session(session)
                .status(Order.OrderStatus.PENDING)
                .build();

        List<OrderItem> orderItems = new ArrayList<>();

        // 3. Process Items
        for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemRequest.getProductId()));

            if (!product.getIsAvailable()) {
                throw new IllegalStateException("Product is not available: " + product.getName());
            }

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .unitPriceSnapshot(product.getPrice())
                    .subtotal(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())))
                    .notes(itemRequest.getNotes())
                    .build();

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);

        // 4. Save Order
        return orderRepository.save(order);
    }

    public List<Order> getOrdersBySessionToken(String token) {
        DiningSession session = diningSessionRepository.findByAccessToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid session token"));
        return orderRepository.findBySessionId(session.getId());
    }

    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getAllActiveOrders() {
        // Return orders that are not COMPLETED or CANCELLED (assuming we want PENDING/COOKING/SERVED)
        // For simplicity, let's just return all for now or filter in memory if complex query needed
        // Or better, let's just fetch PENDING and COOKING
        List<Order> pending = orderRepository.findByStatus(Order.OrderStatus.PENDING);
        List<Order> cooking = orderRepository.findByStatus(Order.OrderStatus.COOKING);
        List<Order> all = new ArrayList<>();
        all.addAll(pending);
        all.addAll(cooking);
        return all;
    }

    @Transactional
    public Order updateOrderStatus(UUID orderId, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}
