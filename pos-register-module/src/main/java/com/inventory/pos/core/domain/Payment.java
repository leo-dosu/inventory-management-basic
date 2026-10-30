package com.inventory.pos.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Invoice invoice;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate paidOn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    private String note;

    protected Payment() {
    }

    Payment(Invoice invoice, BigDecimal amount, LocalDate paidOn, PaymentMethod method, String note) {
        this.invoice = invoice;
        this.amount = amount;
        this.paidOn = paidOn;
        this.method = method;
        this.note = note;
    }

    public Long getId() { return id; }
    public Invoice getInvoice() { return invoice; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getPaidOn() { return paidOn; }
    public PaymentMethod getMethod() { return method; }
    public String getNote() { return note; }
}
