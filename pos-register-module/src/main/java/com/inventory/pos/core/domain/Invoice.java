package com.inventory.pos.core.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A sale. The totals are fixed when the invoice is created; only {@code paid} changes afterwards, as payments arrive.
 * A sale with no customer is a walk-in and must be paid in full at the register.
 */
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    @ManyToOne
    private Customer customer;

    @Column(nullable = false)
    private LocalDate issuedOn;

    @Column(nullable = false)
    private LocalDate dueOn;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 6, scale = 4)
    private BigDecimal taxRate = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal tax = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal paid = BigDecimal.ZERO;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<InvoiceLine> lines = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<Payment> payments = new ArrayList<>();

    protected Invoice() {
    }

    public Invoice(String number, Customer customer, LocalDate issuedOn, LocalDate dueOn, BigDecimal taxRate) {
        this.number = number;
        this.customer = customer;
        this.issuedOn = issuedOn;
        this.dueOn = dueOn;
        this.taxRate = taxRate;
    }

    public InvoiceLine addLine(Long productId, String sku, String description, int quantity, BigDecimal unitPrice) {
        InvoiceLine line = new InvoiceLine(this, productId, sku, description, quantity, unitPrice);
        lines.add(line);
        subtotal = subtotal.add(line.lineTotal());
        recalculate();
        return line;
    }

    /** Applies a discount in naira. Call after the lines are added. */
    public void applyDiscount(BigDecimal amount) {
        this.discount = amount;
        recalculate();
    }

    public Payment addPayment(BigDecimal amount, LocalDate paidOn, PaymentMethod method, String note) {
        Payment payment = new Payment(this, amount, paidOn, method, note);
        payments.add(payment);
        paid = paid.add(amount);
        return payment;
    }

    private void recalculate() {
        BigDecimal taxable = subtotal.subtract(discount);
        tax = taxOn(taxable, taxRate);
        total = taxable.add(tax);
    }

    /** Tax on an amount, rounded to the kobo. The register uses this too, so its preview always matches the invoice. */
    public static BigDecimal taxOn(BigDecimal taxable, BigDecimal rate) {
        return taxable.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal balance() {
        return total.subtract(paid);
    }

    public InvoiceStatus status(LocalDate today) {
        if (balance().signum() <= 0) {
            return InvoiceStatus.PAID;
        }
        if (dueOn.isBefore(today)) {
            return InvoiceStatus.OVERDUE;
        }
        return paid.signum() > 0 ? InvoiceStatus.PARTIAL : InvoiceStatus.UNPAID;
    }

    public Long getId() { return id; }
    public String getNumber() { return number; }
    public Customer getCustomer() { return customer; }
    public LocalDate getIssuedOn() { return issuedOn; }
    public LocalDate getDueOn() { return dueOn; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getDiscount() { return discount; }
    public BigDecimal getTaxRate() { return taxRate; }
    public BigDecimal getTax() { return tax; }
    public BigDecimal getTotal() { return total; }
    public BigDecimal getPaid() { return paid; }
    public List<InvoiceLine> getLines() { return lines; }
    public List<Payment> getPayments() { return payments; }
}
