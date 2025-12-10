package com.chiranat.pos.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DiningSessionDto {
    private UUID id;
    private String accessToken;
    private String qrCodeUrl;
    private LocalDateTime startTime;
    private Integer customerCount;
    private String status;
}
