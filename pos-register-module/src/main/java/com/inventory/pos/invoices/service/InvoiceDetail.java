package com.inventory.pos.invoices.service;

import com.inventory.pos.core.domain.Invoice;
import com.inventory.pos.core.domain.InvoiceLine;
import com.inventory.pos.core.domain.Payment;
import com.inventory.pos.core.domain.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record InvoiceDetail(InvoiceSummary summary, BigDecimal subtotal, BigDecimal discount, BigDecimal taxRate,
                            BigDecimal tax, List<Line> lines, List<PaymentEntry> payments) {

    public record Line(String sku, String description, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
    }

    public record PaymentEntry(LocalDate paidOn, BigDecimal amount, PaymentMethod method, String note) {
    }

    public static InvoiceDetail of(Invoice invoice, LocalDate today) {
        List<Line> lines = invoice.getLines().stream()
                .map((InvoiceLine l) -> new Line(l.getSku(), l.getDescription(), l.getQuantity(), l.getUnitPrice(),
                        l.lineTotal()))
                .toList();
        List<PaymentEntry> payments = invoice.getPayments().stream()
                .map((Payment p) -> new PaymentEntry(p.getPaidOn(), p.getAmount(), p.getMethod(), p.getNote()))
                .toList();
        return new InvoiceDetail(InvoiceSummary.of(invoice, today), invoice.getSubtotal(), invoice.getDiscount(),
                invoice.getTaxRate(), invoice.getTax(), lines, payments);
    }
}
