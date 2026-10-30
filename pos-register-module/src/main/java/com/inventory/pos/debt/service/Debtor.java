package com.inventory.pos.debt.service;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A customer who owes money. {@code maxDaysOverdue} is how late the oldest overdue invoice is, and {@code dueSoon}
 * means an invoice that is not yet late falls due within the next seven days.
 */
public record Debtor(Long customerId, String name, String phone, String email, long unpaidInvoices,
                     BigDecimal balance, boolean overdue, long maxDaysOverdue, boolean dueSoon,
                     LocalDate earliestDue) {
}
