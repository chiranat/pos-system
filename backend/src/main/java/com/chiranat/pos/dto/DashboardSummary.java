package com.chiranat.pos.dto;

import com.chiranat.pos.model.Transaction;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DashboardSummary {
    private BigDecimal totalSales;
    private Integer totalTransactions;
    private Map<String, BigDecimal> salesByPaymentMethod;
    private List<TopSellingItem> topSellingItems;
    private List<Transaction> recentTransactions;
    private List<SalesTrendDto> salesTrend;
    private List<TableUsageDto> tableUsageStats;
}
