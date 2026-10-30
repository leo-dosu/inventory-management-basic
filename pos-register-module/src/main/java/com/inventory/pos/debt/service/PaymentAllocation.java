package com.inventory.pos.debt.service;

import java.math.BigDecimal;

/** How much of a payment goes to one invoice, and what that invoice still owes afterwards. */
public record PaymentAllocation(String invoiceNumber, BigDecimal applied, BigDecimal remainingBalance) {
}
