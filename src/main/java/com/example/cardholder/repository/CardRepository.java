package com.example.cardholder.repository;

import com.example.cardholder.model.CardDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<CardDetails, Long> {
    
    /**
     * Find all CardDetails by the last four digits of the PAN
     * @param panLastFour the last four digits of the PAN to search for
     * @return a list of CardDetails matching the last four digits
     */
    List<CardDetails> findByPanLastFour(String panLastFour);
}

