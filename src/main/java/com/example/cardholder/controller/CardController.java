package com.example.cardholder.controller;

import com.example.cardholder.dto.AddCardRequest;
import com.example.cardholder.dto.CardResponse;
import com.example.cardholder.model.CardDetails;
import com.example.cardholder.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * POST endpoint to add a new card
     * @param request the card request containing cardholder name and PAN
     * @return ResponseEntity with the created card details
     */
    @PostMapping
    public ResponseEntity<CardDetails> addCard(@RequestBody AddCardRequest request) {
        try {
            CardDetails savedCard = cardService.addCard(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * GET endpoint to search cards by last four digits
     * @param last4 the last four digits of the PAN to search for
     * @return ResponseEntity with list of matching cards
     */
    @GetMapping
    public ResponseEntity<List<CardResponse>> searchByLastFour(@RequestParam String last4) {
        try {
            List<CardResponse> cards = cardService.searchByLastFour(last4);
            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
