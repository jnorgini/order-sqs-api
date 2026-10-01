package com.norgini.dto;

public record OrderEvent(String eventId, Long clientId, Double amount) {
}