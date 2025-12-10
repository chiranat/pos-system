package com.chiranat.pos.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class OrderRequest {
    private String token;
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private UUID productId;
        private Integer quantity;
        private String notes;
    }
}
