package com.chiranat.pos.service;

import com.chiranat.pos.dto.DashboardSummary;
import com.chiranat.pos.dto.SalesTrendDto;
import com.chiranat.pos.dto.TableUsageDto;
import com.chiranat.pos.dto.TopSellingItem;
import com.chiranat.pos.model.DiningSession;
import com.chiranat.pos.model.Order;
import com.chiranat.pos.model.OrderItem;
import com.chiranat.pos.model.Transaction;
import com.chiranat.pos.repository.OrderRepository;
import com.chiranat.pos.repository.TableRepository;
import com.chiranat.pos.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;

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

        // Calculate Table Usage
        Map<String, Long> usageMap = sessions.stream()
                .filter(s -> s.getTable() != null)
                .collect(Collectors.groupingBy(
                        s -> s.getTable().getTableNumber(),
                        Collectors.counting()
                ));

        List<TableUsageDto> tableUsageStats = tableRepository.findAll().stream()
                .map(table -> TableUsageDto.builder()
                        .tableName(table.getTableNumber())
                        .usageCount(usageMap.getOrDefault(table.getTableNumber(), 0L))
                        .build())
                .sorted((a, b) -> b.getUsageCount().compareTo(a.getUsageCount()))
                .collect(Collectors.toList());

        // Calculate Sales Trend
        long daysDiff = java.time.temporal.ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate());
        List<SalesTrendDto> salesTrend = new ArrayList<>();

        if (daysDiff == 0) {
            // Daily: 00:00 - 23:00
            DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("HH:00");
            Map<String, BigDecimal> trendMap = transactions.stream()
                    .collect(Collectors.groupingBy(
                            t -> t.getPaidAt().format(dbFormatter),
                            Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                    ));

            for (int i = 0; i < 24; i++) {
                String label = String.format("%02d:00", i);
                salesTrend.add(SalesTrendDto.builder()
                        .label(label)
                        .amount(trendMap.getOrDefault(label, BigDecimal.ZERO))
                        .build());
            }
        } else if (daysDiff <= 31) {
            // Monthly: Day 1 - End
            DateTimeFormatter dbFormatter = DateTimeFormatter.ISO_DATE;
            DateTimeFormatter labelFormatter = DateTimeFormatter.ofPattern("dd/MM");
            
            Map<String, BigDecimal> trendMap = transactions.stream()
                    .collect(Collectors.groupingBy(
                            t -> t.getPaidAt().format(dbFormatter),
                            Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                    ));

            LocalDate current = start.toLocalDate();
            LocalDate endDate = end.toLocalDate();
            while (!current.isAfter(endDate)) {
                String key = current.format(dbFormatter);
                salesTrend.add(SalesTrendDto.builder()
                        .label(current.format(labelFormatter))
                        .amount(trendMap.getOrDefault(key, BigDecimal.ZERO))
                        .build());
                current = current.plusDays(1);
            }
        } else {
            // Yearly: Month 1 - 12
            DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
            DateTimeFormatter labelFormatter = DateTimeFormatter.ofPattern("MMM yyyy");

            Map<String, BigDecimal> trendMap = transactions.stream()
                    .collect(Collectors.groupingBy(
                            t -> t.getPaidAt().format(dbFormatter),
                            Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                    ));

            LocalDate current = start.toLocalDate().withDayOfMonth(1);
            LocalDate endDate = end.toLocalDate().withDayOfMonth(1);
            
            while (!current.isAfter(endDate)) {
                String key = current.format(dbFormatter);
                salesTrend.add(SalesTrendDto.builder()
                        .label(current.format(labelFormatter))
                        .amount(trendMap.getOrDefault(key, BigDecimal.ZERO))
                        .build());
                current = current.plusMonths(1);
            }
        }

        return DashboardSummary.builder()
                .totalSales(totalSales)
                .totalTransactions(transactions.size())
                .salesByPaymentMethod(salesByPaymentMethod)
                .topSellingItems(topSellingItems)
                .recentTransactions(transactions)
                .salesTrend(salesTrend)
                .tableUsageStats(tableUsageStats)
                .build();
    }

    public DashboardSummary getDailySummary(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return getSummary(startOfDay, endOfDay);
    }
}
