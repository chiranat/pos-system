package com.chiranat.pos.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TopSellingItem {
    private String productName;
    private Integer quantitySold;
    private BigDecimal totalRevenue;
}
