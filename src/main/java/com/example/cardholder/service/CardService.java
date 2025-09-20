package com.example.cardholder.service;

import com.example.cardholder.dto.AddCardRequest;
import com.example.cardholder.dto.CardResponse;
import com.example.cardholder.model.CardDetails;
import com.example.cardholder.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final EncryptionService encryptionService;

    public CardService(CardRepository cardRepository, EncryptionService encryptionService) {
        this.cardRepository = cardRepository;
        this.encryptionService = encryptionService;
    }

    /**
     * Adds a new card to the database
     * @param request the card request containing cardholder name and PAN
     * @return the saved CardDetails entity
     */
    public CardDetails addCard(AddCardRequest request) {
        // Extract the last 4 digits from the PAN
        String pan = request.pan();
        String lastFour = pan.substring(pan.length() - 4);
        
        // Encrypt the full PAN
        String encryptedPan = encryptionService.encrypt(pan);
        
        // Create new CardDetails entity
        CardDetails cardDetails = new CardDetails();
        cardDetails.setCardholderName(request.cardholderName());
        cardDetails.setEncryptedPan(encryptedPan);
        cardDetails.setPanLastFour(lastFour);
        
        // Save to database
        return cardRepository.save(cardDetails);
    }

    /**
     * Searches for cards by the last four digits of the PAN
     * @param lastFour the last four digits to search for
     * @return list of CardResponse DTOs
     */
    public List<CardResponse> searchByLastFour(String lastFour) {
        // Find all cards matching the last four digits
        List<CardDetails> cards = cardRepository.findByPanLastFour(lastFour);
        
        // Convert each CardDetails to CardResponse
        return cards.stream()
                .map(this::convertToCardResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converts CardDetails entity to CardResponse DTO
     * @param cardDetails the entity to convert
     * @return the corresponding DTO
     */
    private CardResponse convertToCardResponse(CardDetails cardDetails) {
        String maskedPan = generateMaskedPan(cardDetails.getPanLastFour());
        return new CardResponse(
                cardDetails.getCardholderName(),
                maskedPan,
                cardDetails.getCreatedAt()
        );
    }

    /**
     * Generates a masked PAN in the format **** **** **** 1234
     * @param lastFour the last four digits of the PAN
     * @return the masked PAN string
     */
    private String generateMaskedPan(String lastFour) {
        return "**** **** **** " + lastFour;
    }
}
