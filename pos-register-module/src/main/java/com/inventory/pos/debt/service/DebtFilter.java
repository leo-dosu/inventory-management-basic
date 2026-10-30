package com.inventory.pos.debt.service;

public enum DebtFilter {
    ALL("All Debtors"),
    OVERDUE("Overdue Only"),
    DUE_SOON("Due in 7 Days");

    private final String label;

    DebtFilter(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public boolean matches(Debtor debtor) {
        return switch (this) {
            case ALL -> true;
            case OVERDUE -> debtor.overdue();
            case DUE_SOON -> debtor.dueSoon();
        };
    }
}
