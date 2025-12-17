package com.chiranat.pos.service;

import com.chiranat.pos.dto.DashboardSummary;
import com.chiranat.pos.dto.TopSellingItem;
import com.chiranat.pos.model.DiningSession;
import com.chiranat.pos.model.Order;
import com.chiranat.pos.model.OrderItem;
import com.chiranat.pos.model.Transaction;
import com.chiranat.pos.repository.OrderRepository;
import com.chiranat.pos.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;

    public DashboardSummary getSummary(LocalDateTime start, LocalDateTime end) {
        List<Transaction> transactions = transactionRepository.findAllByPaidAtBetweenOrderByPaidAtDesc(start, end);

        BigDecimal totalSales = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> salesByPaymentMethod = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getPaymentMethod().name(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        // Calculate Top Selling Items
        List<DiningSession> sessions = transactions.stream()
                .map(Transaction::getSession)
                .distinct()
                .collect(Collectors.toList());

        List<TopSellingItem> topSellingItems = Collections.emptyList();
        
        if (!sessions.isEmpty()) {
            List<Order> orders = orderRepository.findBySessionIn(sessions);
            
            Map<String, List<OrderItem>> itemsByProduct = orders.stream()
                    .flatMap(order -> order.getOrderItems().stream())
                    .collect(Collectors.groupingBy(item -> item.getProduct().getName()));

            topSellingItems = itemsByProduct.entrySet().stream()
                    .map(entry -> {
                        String productName = entry.getKey();
                        List<OrderItem> items = entry.getValue();
                        
                        int totalQuantity = items.stream()
                                .mapToInt(OrderItem::getQuantity)
                                .sum();
                                
                        BigDecimal totalRevenue = items.stream()
                                .map(OrderItem::getSubtotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                                
                        return TopSellingItem.builder()
                                .productName(productName)
                                .quantitySold(totalQuantity)
                                .totalRevenue(totalRevenue)
                                .build();
                    })
                    .sorted((a, b) -> b.getQuantitySold().compareTo(a.getQuantitySold()))
                    .limit(5)
                    .collect(Collectors.toList());
        }

        return DashboardSummary.builder()
                .totalSales(totalSales)
                .totalTransactions(transactions.size())
                .salesByPaymentMethod(salesByPaymentMethod)
                .topSellingItems(topSellingItems)
                .recentTransactions(transactions)
                .build();
    }

    public DashboardSummary getDailySummary(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return getSummary(startOfDay, endOfDay);
    }
}
