package com.innostore.improvementhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Java 21 Record for Partner Registration Request
 * Immutable data transfer object with automatic constructor, getters, equals, hashCode, and toString
 */
public record PartnerRegistrationRequest(
    @NotBlank(message = "Company name is required")
    String companyName,

    String vatNumber,

    @NotBlank(message = "Contact person is required")
    String contactPerson,

    String streetAddress,
    String city,
    String postalCode,
    String country,

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    String email,

    // Fields from partner registration form
    String innovationInterests,
    String industriesMarkets,
    String visionGrowth,
    String competencesResources
) {}