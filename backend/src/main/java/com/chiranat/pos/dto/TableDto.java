package com.chiranat.pos.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class TableDto {
    private UUID id;
    private String tableNumber;
    private Integer capacity;
    private String status;
    private DiningSessionDto currentSession;
}
