package com.inventory.pos.core.domain;

public enum PaymentMethod {
    CASH("Cash"),
    TRANSFER("Bank transfer"),
    CARD("Card");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
