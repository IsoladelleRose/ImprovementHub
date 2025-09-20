package com.innostore.improvementhub.repository;

import com.innostore.improvementhub.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    // Find partner by email
    Optional<Partner> findByEmail(String email);

    // Check if partner exists by email
    boolean existsByEmail(String email);

    // Find partners by company name (case insensitive)
    java.util.List<Partner> findByCompanyNameContainingIgnoreCase(String companyName);
}