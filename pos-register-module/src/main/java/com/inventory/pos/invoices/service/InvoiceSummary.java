package com.inventory.pos.invoices.service;

import com.inventory.pos.core.domain.Invoice;
import com.inventory.pos.core.domain.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** One row of the invoice ledger. {@code customerId} is null for a walk-in sale. */
public record InvoiceSummary(Long id, String number, Long customerId, String customerName, String customerPhone,
                             LocalDate issuedOn, LocalDate dueOn, BigDecimal total, BigDecimal paid,
                             BigDecimal balance, InvoiceStatus status, long daysOverdue) {

    public static final String WALK_IN = "Walk-in Retail Guest";

    public static InvoiceSummary of(Invoice invoice, LocalDate today) {
        InvoiceStatus status = invoice.status(today);
        long daysOverdue = status == InvoiceStatus.OVERDUE ? ChronoUnit.DAYS.between(invoice.getDueOn(), today) : 0;
        var customer = invoice.getCustomer();
        return new InvoiceSummary(invoice.getId(), invoice.getNumber(),
                customer == null ? null : customer.getId(),
                customer == null ? WALK_IN : customer.getName(),
                customer == null ? null : customer.getPhone(),
                invoice.getIssuedOn(), invoice.getDueOn(), invoice.getTotal(), invoice.getPaid(),
                invoice.balance(), status, daysOverdue);
    }
}
