package com.innostore.improvementhub.dto;

import java.time.LocalDateTime;

/**
 * Java 21 Record for Partner Response
 * Immutable response object for partner data
 */
public record PartnerResponse(
    Long id,
    String companyName,
    String contactPerson,
    String email,
    String city,
    String country,
    LocalDateTime createdAt
) {}