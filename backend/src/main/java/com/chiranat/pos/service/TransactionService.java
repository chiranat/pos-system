package com.chiranat.pos.service;

import com.chiranat.pos.dto.PaymentRequest;
import com.chiranat.pos.dto.ReceiptDTO;
import com.chiranat.pos.dto.ReceiptItemDTO;
import com.chiranat.pos.exception.ResourceNotFoundException;
import com.chiranat.pos.model.*;
import com.chiranat.pos.repository.DiningSessionRepository;
import com.chiranat.pos.repository.OrderRepository;
import com.chiranat.pos.repository.TableRepository;
import com.chiranat.pos.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final DiningSessionRepository diningSessionRepository;
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;

    @Transactional
    public Transaction processPayment(UUID tableId, PaymentRequest request) {
        // 1. Find Active Session
        DiningSession session = diningSessionRepository.findByTableIdAndStatus(tableId, DiningSession.SessionStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active session found for table: " + tableId));

        // 2. Calculate Total Amount
        List<Order> orders = orderRepository.findBySessionId(session.getId());
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Order order : orders) {
            if (order.getStatus() != Order.OrderStatus.CANCELLED) {
                for (OrderItem item : order.getOrderItems()) {
                    totalAmount = totalAmount.add(item.getSubtotal());
                }
                // Update order status to COMPLETED if it was SERVED
                if (order.getStatus() == Order.OrderStatus.SERVED) {
                    order.setStatus(Order.OrderStatus.CONFIRMED); // Or keep as SERVED? Let's say completed flow doesn't change order status strictly or maybe we should have a PAID status?
                    // For now, let's leave order status as is, or maybe mark them as completed?
                    // The requirement is just payment.
                }
            }
        }

        // 3. Create Transaction
        Transaction transaction = Transaction.builder()
                .session(session)
                .amount(totalAmount)
                .paymentMethod(request.getPaymentMethod())
                .status(Transaction.TransactionStatus.COMPLETED)
                .build();
        
        transactionRepository.save(transaction);

        // 4. Close Session
        session.setStatus(DiningSession.SessionStatus.COMPLETED);
        session.setEndTime(LocalDateTime.now());
        diningSessionRepository.save(session);

        // 5. Free up Table
        TableEntity table = session.getTable();
        table.setStatus(TableEntity.TableStatus.AVAILABLE);
        tableRepository.save(table);

        return transaction;
    }

    public ReceiptDTO getReceipt(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + transactionId));

        DiningSession session = transaction.getSession();
        List<Order> orders = orderRepository.findBySessionId(session.getId());

        // Flatten all order items
        List<OrderItem> allItems = orders.stream()
                .filter(order -> order.getStatus() != Order.OrderStatus.CANCELLED)
                .flatMap(order -> order.getOrderItems().stream())
                .collect(Collectors.toList());

        // Group by product name to aggregate quantities
        Map<String, List<OrderItem>> groupedItems = allItems.stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getName()));

        List<ReceiptItemDTO> receiptItems = new ArrayList<>();
        
        for (Map.Entry<String, List<OrderItem>> entry : groupedItems.entrySet()) {
            String productName = entry.getKey();
            List<OrderItem> items = entry.getValue();
            
            int totalQty = items.stream().mapToInt(OrderItem::getQuantity).sum();
            BigDecimal totalPrice = items.stream()
                    .map(OrderItem::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // Assuming unit price is consistent, take the first one
            BigDecimal unitPrice = items.isEmpty() ? BigDecimal.ZERO : items.get(0).getUnitPriceSnapshot();

            receiptItems.add(ReceiptItemDTO.builder()
                    .productName(productName)
                    .quantity(totalQty)
                    .price(unitPrice)
                    .subtotal(totalPrice)
                    .build());
        }

        return ReceiptDTO.builder()
                .transactionId(transaction.getId())
                .tableNumber(session.getTable().getTableNumber())
                .paidAt(transaction.getPaidAt())
                .paymentMethod(transaction.getPaymentMethod().name())
                .items(receiptItems)
                .totalAmount(transaction.getAmount())
                .build();
    }
}
