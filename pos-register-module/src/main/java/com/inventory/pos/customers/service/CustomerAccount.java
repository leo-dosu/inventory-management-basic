package com.inventory.pos.customers.service;

import java.math.BigDecimal;

/** A customer with the figures shown on their card: how many orders, total spend and what they still owe. */
public record CustomerAccount(Long id, String name, String phone, String email, String address, String notes,
                              int termsDays, long orders, BigDecimal spend, BigDecimal debt) {

    public boolean owes() {
        return debt.signum() > 0;
    }
}
