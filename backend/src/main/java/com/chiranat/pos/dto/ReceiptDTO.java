package com.chiranat.pos.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ReceiptDTO {
    private UUID transactionId;
    private String tableNumber;
    private LocalDateTime paidAt;
    private String paymentMethod;
    private List<ReceiptItemDTO> items;
    private BigDecimal totalAmount;
    private BigDecimal subtotal;
    private BigDecimal vat; // Assuming 7% included or excluded, let's just show total for now or calculate it
}
