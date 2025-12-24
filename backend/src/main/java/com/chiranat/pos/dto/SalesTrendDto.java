package com.chiranat.pos.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class SalesTrendDto {
    private String label;
    private BigDecimal amount;
}
