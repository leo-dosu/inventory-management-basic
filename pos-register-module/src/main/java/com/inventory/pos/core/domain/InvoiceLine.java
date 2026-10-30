package com.inventory.pos.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * One line of an invoice. It copies the SKU, name and price at the time of sale, so later edits or deletion of the
 * product never change an old invoice. A custom item has no product id.
 */
@Entity
@Table(name = "invoice_lines")
public class InvoiceLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Invoice invoice;

    private Long productId;

    private String sku;

    @Column(nullable = false)
    private String description;

    private int quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    protected InvoiceLine() {
    }

    InvoiceLine(Invoice invoice, Long productId, String sku, String description, int quantity, BigDecimal unitPrice) {
        this.invoice = invoice;
        this.productId = productId;
        this.sku = sku;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getSku() { return sku; }
    public String getDescription() { return description; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
}
