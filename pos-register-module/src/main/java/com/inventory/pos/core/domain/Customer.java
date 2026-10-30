package com.inventory.pos.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String phone;
    private String email;
    private String address;

    @Column(length = 500)
    private String notes;

    /** Days a credit invoice stays open before it counts as overdue. */
    private int termsDays = 14;

    protected Customer() {
    }

    public Customer(String name, String phone, String email, String address, String notes, int termsDays) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.notes = notes;
        this.termsDays = termsDays;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getNotes() { return notes; }
    public int getTermsDays() { return termsDays; }
}
