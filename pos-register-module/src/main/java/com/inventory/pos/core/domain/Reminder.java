package com.inventory.pos.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/** A record that a payment reminder was prepared for a customer. Nothing is sent from here: it is the audit trail. */
@Entity
@Table(name = "reminders")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Customer customer;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 1000)
    private String message;

    protected Reminder() {
    }

    public Reminder(Customer customer, LocalDateTime createdAt, String message) {
        this.customer = customer;
        this.createdAt = createdAt;
        this.message = message;
    }

    public Long getId() { return id; }
    public Customer getCustomer() { return customer; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getMessage() { return message; }
}
