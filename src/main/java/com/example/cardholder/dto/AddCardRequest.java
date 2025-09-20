package com.example.cardholder.dto;

public record AddCardRequest(
        String cardholderName,
        String pan
) {
}
