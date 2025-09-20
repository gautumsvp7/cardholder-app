package com.example.cardholder.dto;

import java.time.LocalDateTime;

public record CardResponse(
        String cardholderName,
        String maskedPan,
        LocalDateTime createdAt
) {
}
