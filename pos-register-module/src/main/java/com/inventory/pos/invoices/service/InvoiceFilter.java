package com.inventory.pos.invoices.service;

import com.inventory.pos.core.domain.InvoiceStatus;

public enum InvoiceFilter {
    ALL("All"),
    OVERDUE("Overdue Debt"),
    UNPAID("Unpaid Credit"),
    PARTIAL("Partial"),
    PAID("Paid");

    private final String label;

    InvoiceFilter(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public boolean matches(InvoiceStatus status) {
        return switch (this) {
            case ALL -> true;
            case OVERDUE -> status == InvoiceStatus.OVERDUE;
            case UNPAID -> status == InvoiceStatus.UNPAID;
            case PARTIAL -> status == InvoiceStatus.PARTIAL;
            case PAID -> status == InvoiceStatus.PAID;
        };
    }
}
