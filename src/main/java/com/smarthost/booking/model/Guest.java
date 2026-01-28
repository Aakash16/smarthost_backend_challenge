package com.smarthost.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "guest")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_id")
    private Long guestId;

    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Contact is required")
    @Column(name = "contact", nullable = false)
    private String contact;

    @NotNull(message = "MaxBudget is required")
    @Min(value = 0, message = "MaxBudget must be non-negative")
    @Column(name = "max_budget", nullable = false)
    private Double maxBudget;

    // Default constructor
    public Guest() {}

    // Constructor
    public Guest(String name, String contact, Double maxBudget) {
        this.name = name;
        this.contact = contact;
        this.maxBudget = maxBudget;
    }

    // Getters and Setters
    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Double getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(Double maxBudget) {
        this.maxBudget = maxBudget;
    }
}