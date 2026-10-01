package com.norgini.dto;

import java.time.LocalDateTime;

public record OrderResponse(Long id, Long clientId, Double amount, String status, LocalDateTime createdAt) {
}
