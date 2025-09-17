package com.innostore.improvementhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PartnerRegistrationRequest {

    @NotBlank(message = "Company name is required")
    private String companyName;

    private String vatNumber;

    @NotBlank(message = "Contact person is required")
    private String contactPerson;

    private String streetAddress;

    private String city;

    private String postalCode;

    private String country;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    // Fields from partner registration form
    private String innovationInterests;
    private String industriesMarkets;
    private String visionGrowth;
    private String competencesResources;

    // Default constructor
    public PartnerRegistrationRequest() {}

    // Getters and Setters
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInnovationInterests() {
        return innovationInterests;
    }

    public void setInnovationInterests(String innovationInterests) {
        this.innovationInterests = innovationInterests;
    }

    public String getIndustriesMarkets() {
        return industriesMarkets;
    }

    public void setIndustriesMarkets(String industriesMarkets) {
        this.industriesMarkets = industriesMarkets;
    }

    public String getVisionGrowth() {
        return visionGrowth;
    }

    public void setVisionGrowth(String visionGrowth) {
        this.visionGrowth = visionGrowth;
    }

    public String getCompetencesResources() {
        return competencesResources;
    }

    public void setCompetencesResources(String competencesResources) {
        this.competencesResources = competencesResources;
    }
}