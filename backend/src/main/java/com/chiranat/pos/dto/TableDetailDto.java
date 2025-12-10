package com.chiranat.pos.dto;

import com.chiranat.pos.model.Order;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class TableDetailDto {
    private TableDto table;
    private DiningSessionDto currentSession;
    private List<Order> orders;
    private BigDecimal totalAmount;
}
