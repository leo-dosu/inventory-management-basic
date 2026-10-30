package com.inventory.pos.debt.service;

import java.math.BigDecimal;

/** The four cards on Debt Recovery. {@code recovered} is every payment received so far, at the till or by transfer. */
public record DebtOverview(BigDecimal outstanding, long openInvoices, BigDecimal overdueArrears,
                           BigDecimal recovered, long debtorAccounts) {
}
