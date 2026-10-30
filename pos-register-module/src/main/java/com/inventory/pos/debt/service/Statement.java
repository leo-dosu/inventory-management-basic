package com.inventory.pos.debt.service;

import com.inventory.pos.invoices.service.InvoiceSummary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** A customer's statement of account: every invoice with its balance, as at {@code asOf}. */
public record Statement(Long customerId, String customerName, String phone, String email, String address,
                        int termsDays, List<InvoiceSummary> invoices, BigDecimal totalBilled, BigDecimal totalPaid,
                        BigDecimal totalOutstanding, LocalDate asOf) {
}
